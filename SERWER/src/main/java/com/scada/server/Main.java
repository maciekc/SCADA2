package com.scada.server;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.actor.ActorRef;
import static akka.pattern.PatternsCS.ask;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpEntity;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.model.headers.HttpOrigin;
import akka.http.javadsl.model.headers.HttpOriginRange;
import akka.http.javadsl.server.*;
import akka.http.javadsl.unmarshalling.StringUnmarshallers;
import akka.http.javadsl.unmarshalling.Unmarshaller;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import akka.util.Timeout;
import ch.megard.akka.http.cors.javadsl.settings.CorsSettings;

import static ch.megard.akka.http.cors.javadsl.CorsDirectives.cors;
import static ch.megard.akka.http.cors.javadsl.CorsDirectives.corsRejectionHandler;
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
import java.util.NoSuchElementException;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Supplier;


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

    private final Unmarshaller<HttpEntity, LimitUpdate> limitUnmarshaller = Jackson.unmarshaller(LimitUpdate.class);

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
        //OPCDataLogger.tell(new StartLogging(), ActorRef.noSender());

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

            // Your CORS settings
            final CorsSettings settings = CorsSettings.defaultSettings()
                    .withAllowedOrigins(HttpOriginRange.create(HttpOrigin.parse("http://localhost:4200")));

            // Your rejection handler
            final RejectionHandler rejectionHandler = corsRejectionHandler().withFallback(RejectionHandler.defaultHandler());

            // Your exception handler
            final ExceptionHandler exceptionHandler = ExceptionHandler.newBuilder()
                    .match(NoSuchElementException.class, ex -> complete(StatusCodes.NOT_FOUND, ex.getMessage()))
                    .build();

            // Combining the two handlers only for convenience
            final Function<Supplier<Route>, Route> handleErrors = inner -> Directives.allOf(
                    s -> handleExceptions(exceptionHandler, s),
                    s -> handleRejections(rejectionHandler, s),
                    inner
            );

        return handleErrors.apply(() -> cors(settings, () -> handleErrors.apply(() -> route(
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
                pathPrefix("stateVariableData", () -> route(
                    pathEnd(() ->
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
                    path("BAR_DAY", () ->
                            get(() ->
                                    parameter(StringUnmarshallers.STRING,"name", tag -> {
                                        System.out.println("BAR_DAY : " + tag);
                                        final StateVariabeData message = new StateVariableData_dailyMode(tag);
                                        final CompletionStage<Object> futureResult = ask(getStateVariableData, message, t);
                                        return onSuccess(() -> futureResult, result ->
                                                completeOK(result, Jackson.marshaller()));
                                    })
                            )
                    ),
                    path("BAR_HOUR", () ->
                            get(() ->
                                    parameter(StringUnmarshallers.STRING,"name", tag -> {
                                        System.out.println("BAR_HOUR : " + tag);
                                        final StateVariabeData message = new StateVariableData_hourlyMode(tag);
                                        final CompletionStage<Object> futureResult = ask(getStateVariableData, message, t);
                                        return onSuccess(() -> futureResult, result ->
                                                completeOK(result, Jackson.marshaller()));
                                    })
                            )
                    ))
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
                                        final String startDate = param.get(0).getValue().toString();
                                        final String endDate = param.get(1).getValue().toString();
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
                                            final String startDate = param.get(0).getValue().toString();
                                            final String endDate = param.get(1).getValue().toString();
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
                                            final String startDate = param.get(0).getValue().toString();
                                            final String endDate = param.get(1).getValue().toString();
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
                                            final String startDate = param.get(0).getValue().toString();
                                            final String endDate = param.get(1).getValue().toString();
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
                                            final String startDate = param.get(0).getValue().toString();
                                            final String endDate = param.get(1).getValue().toString();
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
                ),
                path("currentData", () ->
                    pathEnd(() ->
                            get(() -> {
                                final CompletionStage<Object> currentSystemData = ask(getStateVariableData, new CurrentSystemData(), t);
                                return onSuccess(() -> currentSystemData, result ->
                                        completeOK(result, Jackson.marshaller()));
                            }
                        ))
                ),
                path("limitsData", () ->
                    get(() ->
                            parameter("stateVariableTag", tag -> {
                                final CompletionStage<Object> currentSystemData = ask(getStateVariableData, new LimitsData(tag), t);
                                return onSuccess(() -> currentSystemData, result ->
                                        completeOK(result, Jackson.marshaller()));
                            }
                    ))
                ),

                //--------------------------------------------------
                //                  POST REQUESTS
                //--------------------------------------------------

                path("limits", () ->
                    post(() ->
                        entity(this.limitUnmarshaller, lu -> {
                                    System.out.println("get " + lu.getValue() + " " + lu.getTag());
                                    return completeOK("OK", Jackson.marshaller());
                                }
                        )
                    )
                )
            ))
        ));
    }
}
