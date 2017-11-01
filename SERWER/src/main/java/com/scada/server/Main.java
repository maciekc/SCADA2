package com.scada.server;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.actor.ActorRef;
import static akka.pattern.PatternsCS.ask;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.http.javadsl.unmarshalling.StringUnmarshallers;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import akka.util.Timeout;
import com.google.gson.Gson;
import com.scada.dataBase.DBUpdates;
import com.scada.dataBase.GetDBData;
import com.scada.dataBase.InsertDataToDB;
import com.scada.dataBase.UpdateDataInDB;

import com.scada.model.dataBase.Work.Work;
import com.scada.server.StateVariableActor.*;
import com.scada.server.ReportDataActor.*;
import com.scada.server.NotificationActor.*;
import com.scada.server.OPCDataLoggerActor.*;

import com.scada.model.dataBase.ChangeParameterValue.ChangeParameterValue;
import com.scada.model.dataBase.Controller.Controller;
import com.scada.model.dataBase.Limit.Limit;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import scala.concurrent.duration.Duration;

import java.io.IOException;
import java.util.concurrent.*;


public class Main extends AllDirectives {

    static Timeout t = new Timeout(Duration.create(5, TimeUnit.SECONDS));

    static final DBI dbi = new DBI("jdbc:mysql://localhost:3306/scada", "root", "1234");
    static final Handle handle = dbi.open();
    static final GetDBData getDBData = new GetDBData(handle);
    static final InsertDataToDB insertDataToDB = new InsertDataToDB(handle);
    static final UpdateDataInDB updateDataInDB = new UpdateDataInDB(handle);
    static ActorRef getStateVariableData = null;
    static ActorRef reportData = null;
    static ActorRef notificationDAta = null;
    static ActorRef OPCDataLogger = null;
    static ActorSystem system = ActorSystem.create("routes");

    public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {

        System.out.println("open bd connection");

        final OPCServer opcServer = new OPCServer();

        getStateVariableData = system.actorOf(StateVariableActor.props(getDBData), "getStateVariableData");
        reportData = system.actorOf(ReportDataActor.props(getDBData), "reportData");
        notificationDAta = system.actorOf(NotificationActor.props(getDBData), "notificationData");
        OPCDataLogger = system.actorOf(OPCDataLoggerActor.props(insertDataToDB, opcServer), "OPCDataLogger");

        //----------------------------------------------------------------
        //                      LOGOWANIE
        //----------------------------------------------------------------
//        OPCDataLogger.tell(new StartLogging(), ActorRef.noSender());

        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        //In order to access all directives we need an instance where the routes are define.
        Main app = new Main();

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute().flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow,
                ConnectHttp.toHost("localhost", 8010), materializer);



        System.out.println("Server online at http://localhost:8010/\nPress RETURN to stop...");
        System.in.read(); // let it run until user presses return

        binding
                .thenCompose(ServerBinding::unbind) // trigger unbinding from the port
                .thenAccept(unbound -> system.terminate()); // and shutdown when done
//

        handle.close();

    }

    private Route createRoute() {
        return route(
                path("selectAll", () ->
                    get(() -> {
                                final StateVariabeData message = new StateVariabeData("VALVE_1", "2017-10-27 15:00:00", "2017-10-29 12:00:00");
                                final CompletionStage<Object> futureResult = ask(getStateVariableData, message, t);
                                System.out.println(futureResult);
                                return onSuccess(() -> futureResult, result ->
                                        completeOK(result, Jackson.marshaller())

                                );
                    })
                ),
                path("stateVariableData", () ->
                    get(() ->
                            parameter(StringUnmarshallers.STRING,"name", tag -> {
                                System.out.println("tag : " + tag);
                                final StateVariabeData message = new StateVariabeData(tag);
                                final CompletionStage<Object> futureResult = ask(getStateVariableData, message, t);
                                return onSuccess(() -> futureResult, result ->
                                        completeOK(result, Jackson.marshaller()));
                            })
                        )
                ),
                path("stateSpace", () ->
                    get(() -> {
                        final CompletionStage<Object> getStateSpace = ask(getStateVariableData, "getStateSpace", t);
                        return onSuccess(() -> getStateSpace, result ->
                            completeOK(result, Jackson.marshaller()));
                    })
                ),

                pathPrefix("andon", () -> route(
                    pathEnd(() ->
                        get(() -> {
                            final CompletionStage<Object> andonData = ask(reportData, new AndonData(), t);
                            return onSuccess(() -> andonData, result ->
                                    completeOK(result, Jackson.marshaller()));
                        })
                    ),
                    path("range", () ->
                            get(() ->
                                    parameterList(param -> {
                                        final String startDate = param.get(0).getValue();
                                        final String endDate = param.get(1).getValue();
                                        final CompletionStage<Object> andonData = ask(reportData, new AndonData(startDate, endDate), t);
                                        return onSuccess(() -> andonData, result -> completeOK(result, Jackson.marshaller()));
                                    })
                            )
                    ))
                ),
                pathPrefix("work", () -> route(
                        pathEnd(() ->
                                get(() -> {
                                    final CompletionStage<Object> workData = ask(reportData, new WorkData(), t);
                                    return onSuccess(() -> workData, result ->
                                            completeOK(result, Jackson.marshaller()));
                                })
                        ),
                        path("range", () ->
                                get(() ->
                                        parameterList(param -> {
                                            final String startDate = param.get(0).getValue();
                                            final String endDate = param.get(1).getValue();
                                            final CompletionStage<Object> workData = ask(reportData, new WorkData(startDate, endDate), t);
                                            return onSuccess(() -> workData, result -> completeOK(result, Jackson.marshaller()));
                                        })
                                )
                        ))
                ),
                pathPrefix("controller", () -> route(
                        pathEnd(() ->
                                get(() -> {
                                    final CompletionStage<Object> controllerData = ask(reportData, new ControllerData(), t);
                                    return onSuccess(() -> controllerData, result ->
                                            completeOK(result, Jackson.marshaller()));
                                })
                        ),
                        path("range", () ->
                                get(() ->
                                        parameterList(param -> {
                                            final String startDate = param.get(0).getValue();
                                            final String endDate = param.get(1).getValue();
                                            final CompletionStage<Object> controllerData = ask(reportData, new ControllerData(startDate, endDate), t);
                                            return onSuccess(() -> controllerData, result -> completeOK(result, Jackson.marshaller()));
                                        })
                                )
                        ))
                ),
                pathPrefix("changeParameter", () -> route(
                        pathEnd(() ->
                                get(() -> {
                                    final CompletionStage<Object> cpvData = ask(reportData, new ChangeParameterValueData(), t);
                                    return onSuccess(() -> cpvData, result ->
                                            completeOK(result, Jackson.marshaller()));
                                })
                        ),
                        path("range", () ->
                                get(() ->
                                        parameterList(param -> {
                                            final String startDate = param.get(0).getValue();
                                            final String endDate = param.get(1).getValue();
                                            final CompletionStage<Object> cpvData = ask(reportData, new ChangeParameterValueData(startDate, endDate), t);
                                            return onSuccess(() -> cpvData, result -> completeOK(result, Jackson.marshaller()));
                                        })
                                )
                        ))
                ),
                pathPrefix("notification", () -> route(
                        pathEnd(() ->
                                get(() -> {
                                    final CompletionStage<Object> notification = ask(notificationDAta, new NotificationData(), t);
                                    return onSuccess(() -> notification, result ->
                                            completeOK(result, Jackson.marshaller()));
                                })
                        ),
                        path("range", () ->
                                get(() ->
                                        parameterList(param -> {
                                            final String startDate = param.get(0).getValue();
                                            final String endDate = param.get(1).getValue();
                                            final CompletionStage<Object> notification = ask(notificationDAta, new NotificationData(startDate, endDate), t);
                                            return onSuccess(() -> notification, result -> completeOK(result, Jackson.marshaller()));
                                        })
                                )
                        ))
                ),
                path("requestAndon", () ->
                        get(() -> {
                            final CompletionStage<Object> andonRequest = ask(notificationDAta, new NotificationActor.AndonRequest(OPCDataLogger), t);
                            return onSuccess(() -> andonRequest, result ->
                                    completeOK(result, Jackson.marshaller()));
                        })
                )


        );
    }
}
