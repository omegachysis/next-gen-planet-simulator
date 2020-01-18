using System;
using System.Numerics;

namespace Capstone
{
    class Program
    {
        static Vector2 Dx(Vector2[,] U, int x, int y)
        {
            var u0 = x >= 1 ? U[x - 1,y] : Vector2.Zero;
            var u1 = x < U.GetLength(0) - 1 ? U[x + 1,y] : Vector2.Zero;
            return u1 - u0;
        }

        static Vector2 Dy(Vector2[,] U, int x, int y)
        {
            var u0 = y >= 1 ? U[x, y - 1] : Vector2.Zero;
            var u1 = y < U.GetLength(0) - 1 ? U[x, y + 1] : Vector2.Zero;
            return u1 - u0;
        }

        static void Main(string[] args)
        {
            var size = 70;
            var U = new Vector2[size,size];
            var Udt = new Vector2[size,size];
            var dt = -0.1f;

            for (int y = 0; y < size; y++)
            {
                for (int x = 0; x < size; x++)
                {
                    U[x, y] = new Vector2(-(x - size / 2), -(y - size / 2)) * 0.1f;
                }
            }

            Console.WriteLine("Ready");
            while (Console.ReadLine() == "")
            {                
                for (int y = 0; y < size; y++)
                {
                    for (int x = 0; x < size; x++)
                    {
                        var u = U[x,y];
                        if (u.Length() <= 0.1f)
                            Console.Write(" ");
                        else
                        {
                            if (Math.Abs(u.X) == Math.Max(Math.Abs(u.X), Math.Abs(u.Y)))
                            {
                                if (u.X < 0)
                                    Console.Write("W");
                                else
                                    Console.Write("E");
                            }
                            if (Math.Abs(u.Y) == Math.Max(Math.Abs(u.X), Math.Abs(u.Y)))
                            {
                                if (u.Y < 0)
                                    Console.Write("S");
                                else
                                    Console.Write("N");
                            }
                        }
                    }

                    Console.WriteLine();
                }

                // Simulate Navier-Stokes
                for (int y = 0; y < size; y++)
                {
                    for (int x = 0; x < size; x++)
                    {
                        var gradU = Dx(U, x, y).X + Dy(U, x, y).Y;
                        
                        Udt[x,y].X = -gradU * U[x,y].X;
                        Udt[x,y].Y = -gradU * U[x,y].Y;
                    }
                }
                for (int y = 0; y < size; y++)
                {
                    for (int x = 0; x < size; x++)
                    {
                        U[x,y] += Udt[x,y] * dt;
                    }
                }
            }
        }

        // static void Main(string[] args)
        // {
        //     var stepSize = 5;
        //     var displaySize = 70;
        //     var num = 3000;
        //     var dt = 0.1f;
        //     var G = 1.0f;
        //     var particleRadius = 1f;
        //     var ps = new Vector2[num];
        //     var vs = new Vector2[num];

        //     var rand = new Random(1);
        //     for (int i = 0; i < num; i++)
        //     {
        //         ps[i] = new Vector2(
        //             (float)rand.NextDouble() * 100f - 50f, 
        //             (float)rand.NextDouble() * 100f - 50f);
        //         vs[i] = new Vector2(
        //             (float)rand.NextDouble() - 0.5f, 
        //             (float)rand.NextDouble() - 0.5f);
        //     }

        //     var display = new bool[displaySize,displaySize];

        //     Console.WriteLine("Ready");
        //     while (Console.ReadLine() == "")
        //     {
        //         for (int n = 0; n < stepSize; n++)
        //         {
        //             // Move particles and print positions.
        //             for (int i = 0; i < num; i++)
        //             {
        //                 ps[i] += vs[i] * dt;
        //             }

        //             // Calculate gravity accelerations.
        //             for (int i = 0; i < num; i++)
        //             {
        //                 for (int j = 0; j < num; j++)
        //                 {
        //                     var r = ps[j] - ps[i];
        //                     if (r.Length() >= particleRadius)
        //                     {
        //                         vs[i] += r * G * dt / (r.Length() * r.LengthSquared());
        //                     }
        //                     else if (i != j)
        //                     {
        //                         var avg = (vs[i] + vs[j]) * 0.5f;
        //                         vs[i] = avg;
        //                         vs[j] = avg;
        //                     }
        //                 }
        //             }
        //         }

        //         // Print particles
        //         for (int y = 0; y < displaySize; y++)
        //         {
        //             for (int x = 0; x < displaySize; x++)
        //             {
        //                 display[x,y] = false;
        //             }
        //         }
        //         for (int i = 0; i < num; i++)
        //         {
        //             var dispX = (int)(ps[i].X + displaySize/2f);
        //             var dispY = (int)(ps[i].Y + displaySize/2f);
        //             if (dispX < displaySize && dispY < displaySize &&
        //                 dispX > 0 && dispY > 0)
        //                 display[dispX, dispY] = true;
        //         }
        //         for (int y = 0; y < displaySize; y++)
        //         {
        //             for (int x = 0; x < displaySize; x++)
        //             {
        //                 Console.Write(display[x,y] ? "*" : " ");
        //             }

        //             Console.WriteLine();
        //         }
        //     }
        // }
    }
}
