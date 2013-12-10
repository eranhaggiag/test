package com.programa17.bitcoin.excel;

import org.boris.xlloop.FunctionServer;
import org.boris.xlloop.handler.*;
import org.boris.xlloop.reflect.*;

public class ExcelServer
{
    public static void main(String[] args) throws Exception {
        // Create function server on the default port
        FunctionServer fs = new FunctionServer();

        // Create a reflection function handler and add the Math methods
        ReflectFunctionHandler rfh = new ReflectFunctionHandler();
        rfh.addMethods("BTCE.", DataExcelProvider.class);

        // Create a function information handler to register our functions
        FunctionInformationHandler firh = new FunctionInformationHandler();
        firh.add(rfh.getFunctions());

        // Set the handlers
        CompositeFunctionHandler cfh = new CompositeFunctionHandler();
        cfh.add(rfh);
        cfh.add(firh);
        fs.setFunctionHandler(new DebugFunctionHandler(cfh));

        // Run the engine
        System.out.println("Listening on port " + fs.getPort() + "...");
        fs.run();
    }
}