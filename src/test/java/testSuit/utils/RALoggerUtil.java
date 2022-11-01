package testSuit.utils;

import io.cucumber.guice.ScenarioScoped;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.filter.log.LogDetail;
import io.restassured.internal.print.RequestPrinter;
import io.restassured.internal.print.ResponsePrinter;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

@Slf4j
@ScenarioScoped
public class RALoggerUtil implements Filter {

    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    private static final String MESSAGE_SEPARATOR = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n";

    public void addToLog( String logMessage ) {
        try {
            byteArrayOutputStream.write( ( logMessage + "\n" ).getBytes() );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public synchronized void logOutput() {
        StringBuilder logs = new StringBuilder( byteArrayOutputStream.toString() );

        log.info(Thread.currentThread().getName()+"\n" + logs.toString().replace("\r", ""));

        byteArrayOutputStream.reset();
    }

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx ) {

        Response response = null;

        try {

            // send the request
            response = ctx.next( requestSpec, responseSpec );

        } catch ( Exception e ) {
            addToLog( "Could not connect to the environment" );
            addToLog( e.getMessage() );
            throw new AssertionError( "Could not connect to the environment" );
        } finally {
            // print the request
            RequestPrinter.print( requestSpec, requestSpec.getMethod(), requestSpec.getURI(), LogDetail.ALL, requestSpec.getConfig().getLogConfig().blacklistedHeaders(), new PrintStream( byteArrayOutputStream ), true );
            // add an empty line
            addToLog( "\n" );
            if ( response != null ) {
                // print the response
                ResponsePrinter.print( response, response, new PrintStream( byteArrayOutputStream ), LogDetail.ALL, true, requestSpec.getConfig().getLogConfig().blacklistedHeaders() );
            }
            // add the message separator
            addToLog( MESSAGE_SEPARATOR );

            // print the log
            logOutput();
        }

        return response;
    }
}
