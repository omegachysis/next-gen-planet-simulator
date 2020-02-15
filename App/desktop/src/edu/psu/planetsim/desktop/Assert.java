package edu.psu.planetsim.desktop;

public class Assert {
    private static int _numPassed = 0;
    private static int _numFailed = 0;

    public static void PrintResults() throws Exception {
        System.out.println("TEST RESULTS:");
        System.out.println("Success: " + _numPassed);
        System.out.println("Failure: " + _numFailed);
        if (_numFailed > 0)
            throw new Exception();
    }

    public static void True(boolean condition, String message) {
        if (!condition) {
            _numFailed += 1;
            System.err.println("Assertion failed: " + message);
        } else {
            _numPassed += 1;
        }
    }
}
