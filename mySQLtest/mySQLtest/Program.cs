using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using MySql.Data.MySqlClient;
using TitaniumAS.Opc.Client.Common;
using TitaniumAS.Opc.Client.Da;
using TitaniumAS.Opc.Client.Da.Browsing;
using System.Threading;

namespace mySQLtest
{
    class Program
    {
        static void Main(string[] args)
        {
            MySQL_Connect bazaDanych = new MySQL_Connect();
            int czasOdpOPC = 5; // Czas odpytywania serwera OPC w sekundach
            string query, ssId, varState, val;
            Console.WriteLine("Oczekiwanie na połączenie z serwerem OPC...");


            // Cykliczne odpytywanie servera OPC
            try
            {
                Uri url = UrlBuilder.Build("Matrikon.OPC.Simulation.1");
                using (var server = new OpcDaServer(url))
                {
                    // Connect to the server first.
                    server.Connect();
                    Console.WriteLine(server.GetStatus().CurrentTime);
                    Console.WriteLine(value: server.IsConnected);

                    String[] list = { "MojaGrupa.OUTPUT", "MojaGrupa.LEVEL 1",
                            "MojaGrupa.LEVEL 2", "MojaGrupa.LEVEL 3",
                            "MojaGrupa.VALVE 1", "MojaGrupa.VALVE 2",
                            "MojaGrupa.VALVE 3","MojaGrupa.VALVE 4"}; // lista tagów które chcemy odczytać w kolejności zdefiniowanej w tabeli State_Space_Id
                    TimeSpan[] tab = new TimeSpan[list.Length];
                    for( int i=0; i<tab.Length; i++)
                    {
                        tab[i] = TimeSpan.FromSeconds(1);
                    }
                    int t = 0;
                    int sim = 0;
                    while (sim<12)
                    {
                        OpcDaVQTE[] vqt = server.Read(list.ToList(), tab.ToList());

                        //  logowanie asynchroniczne
                        // Jezeli wartosc przekracza limit to historii zmienic variable_state_id na andon !!!
                        // 
                        //

                        // logowanie cykliczne
                        if (t == czasOdpOPC)
                        {
                            for (int i = 0; i < list.Length; i++)
                            {
                                // parametry logowania
                                val = vqt[i].Value.ToString();
                                int ssIdInt = i + 1;
                                ssId = ssIdInt.ToString();
                                if (ssIdInt > 1 && ssIdInt < 5)
                                {
                                    varState = VariableState(val, ssIdInt, bazaDanych).ToString();
                                }
                                else
                                    varState = 2.ToString();
                                val = val.Replace(',', '.');
                                query = "INSERT INTO scada.history(variable_state_id, state_space_id, event_id, value, date) VALUES(1,"+ssId+",1," + val + ", now())";
                                //query = "INSERT INTO scada.state_space (tag, name) VALUES ('Output32', 'wyjscie')";
                                //Console.WriteLine(list[i]);
                                //Console.WriteLine(vqt[i].Value.ToString());
                                //Console.WriteLine(query);
                                bazaDanych.Insert(query);
                            }
                            t = 0;
                        }
                        Console.WriteLine(vqt[0].Value.ToString());
                        Console.WriteLine(vqt[0].Quality.ToString());
                        Console.WriteLine(vqt[0].Timestamp.ToString());
                        Console.WriteLine("------------------");
                        t += 1;
                        sim += 1;
                        Thread.Sleep(1000);
                    }
                    server.Disconnect();
                }
            }
            catch (Exception e)
            {
                Console.WriteLine("Cannot connect to OPC server");
                Console.WriteLine(e);
            }
            finally
            {
                //server.Disconnect();
            }
            //Console.ReadKey();
        }

        static int VariableState(String val, int ssId, MySQL_Connect bazaDanych)
        {
            float LL = bazaDanych.LimitValue(ssId, "'LEVEL_"+(ssId-1).ToString()+"_MIN_CRITICAL'");
            float L = bazaDanych.LimitValue(ssId, "'LEVEL_" + (ssId - 1).ToString() + "_MIN'");
            float H = bazaDanych.LimitValue(ssId, "'LEVEL_" + (ssId - 1).ToString() + "_MAX'");
            float HH = bazaDanych.LimitValue(ssId, "'LEVEL_" + (ssId - 1).ToString() + "_MAX_CRITICAL'");
            float value = float.Parse(val);
            if(value>HH)
            {
                return 1;
            }
            else if(value>H)
            {
                return 1;
            }
            else if (value<LL)
            {
                return 1;
            }
            else if (value<L)
            {
                return 1;
            }
            else
                return 2;
        }

    }

    
}
