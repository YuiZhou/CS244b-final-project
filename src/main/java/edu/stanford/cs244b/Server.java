package edu.stanford.cs244b;

import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

import com.bazaarvoice.dropwizard.assets.ConfiguredAssetsBundle;

import io.dropwizard.Application;
import io.dropwizard.Bundle;
import io.dropwizard.Configuration;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jetty.HttpConnectorFactory;
import io.dropwizard.server.SimpleServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerDropwizard;

/** Main server class - the entry-point into our system
 *  Define configuration and any initialization steps here. */
public class Server extends Application<ChordConfiguration> {
    
    
    private final SwaggerDropwizard swaggerDropwizard = new SwaggerDropwizard();
    
    public static void main(String[] args) throws Exception {
        new Server().run(args);
    }

    @Override
    public void initialize(Bootstrap<ChordConfiguration> bootstrap) {
        //bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
        bootstrap.addBundle(new ConfiguredAssetsBundle("/assets/", "/", "index.html"));
        //swaggerDropwizard.onInitialize(bootstrap);
    }

    @Override
    public void run(ChordConfiguration configuration,
                    Environment environment) throws UnknownHostException, NoSuchAlgorithmException {
        environment.jersey().setUrlPattern("/api/*");
        
        // TODO: ensure each shard is uniquely identified/numbered...
        
        // pass in the http server configuration
        SimpleServerFactory serverFactory = (SimpleServerFactory) configuration.getServerFactory();
        HttpConnectorFactory serverConfig = ((HttpConnectorFactory) serverFactory.getConnector());
        
        final Shard shard = new Shard(configuration.getChord(), serverConfig);
        // register the shard endpoint
        environment.jersey().register(shard);
        
        // TODO: automatically discover shards from other servers?
        
        // add api documentation ui
        //swaggerDropwizard.onRun(configuration, environment, "localhost");
    }

}
