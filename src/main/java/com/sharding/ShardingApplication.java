package com.sharding;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.glassfish.jersey.servlet.ServletContainer;

public class ShardingApplication {

    public static void main(String[] args) throws Exception {
        // Default port = 8080; override via --server.port=NNNN
        int port = 8080;
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--server.port=")) {
                port = Integer.parseInt(args[i].substring("--server.port=".length()));
            }
        }

        Server server = new Server(port);

        // Create a context handler for Jetty
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");  // All requests from root

        // Hook up Jersey's ServletContainer
        // Use ShardingJaxRsApp class with @ApplicationPath("/api")
        ServletHolder jerseyHolder = new ServletHolder(ServletContainer.class);
        // Scan resources
        jerseyHolder.setInitParameter(
                "jersey.config.server.provider.packages",
                "com.sharding.resources"  // The package containing your resource classes
        );


        context.addServlet(jerseyHolder, "/*");
        server.setHandler(context);

        server.start();
        System.out.println("Server started on port " + port);
        server.join();
    }
}
