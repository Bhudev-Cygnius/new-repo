package com.myorg.cdk;


import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.constructs.Construct;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.apigateway.RestApi;

public class SessionStack extends Stack {

    public SessionStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // DynamoDB Table for storing sessions
        Table sessionTable = Table.Builder.create(this, "SessionTable")
                .partitionKey(Attribute.builder().name("sessionId").type(AttributeType.STRING).build())
                .tableName("SessionTable")
                .build();

        // Lambda Functions
        Function createSessionFunction = createLambdaFunction("CreateSessionFunction", "com.myorg.handler.CreateSessionHandler");
        Function modifySessionFunction = createLambdaFunction("ModifySessionFunction", "com.myorg.handler.ModifySessionHandler");
        Function requestSessionFunction = createLambdaFunction("RequestSessionFunction", "com.myorg.handler.RequestSessionHandler");
        Function getAvailableSessionsFunction = createLambdaFunction("GetAvailableSessionsFunction", "com.myorg.handler.GetAvailableSessionsHandler");
        Function searchSessionsFunction = createLambdaFunction("SearchSessionsFunction", "com.myorg.handler.SearchSessionsHandler");

        // Grant Lambda functions access to the DynamoDB table
        sessionTable.grantReadWriteData(createSessionFunction);
        sessionTable.grantReadWriteData(modifySessionFunction);
        sessionTable.grantReadWriteData(requestSessionFunction);
        sessionTable.grantReadData(getAvailableSessionsFunction);
        sessionTable.grantReadData(searchSessionsFunction);

        // API Gateway
        RestApi api = RestApi.Builder.create(this, "SessionApi")
                .restApiName("Session API")
                .build();

        // Define API Resources and Integrations
     // Create the base 'session' resource only once
        Resource sessionResource = api.getRoot().addResource("session");

        // POST /session/therapist/{therapistId}
        sessionResource.addResource("therapist").addResource("{therapistId}")
                .addMethod("POST", new LambdaIntegration(createSessionFunction));

        // PUT /session/{sessionId}/therapist/{therapistId}
        Resource sessionIdResource = sessionResource.addResource("{sessionId}");
        sessionIdResource.addResource("therapist").addResource("{therapistId}")
                .addMethod("PUT", new LambdaIntegration(modifySessionFunction));

        // PUT /session/{sessionId}/client/{clientId}
        sessionIdResource.addResource("client").addResource("{clientId}")
                .addMethod("PUT", new LambdaIntegration(requestSessionFunction));

        // GET /session/available
        sessionResource.addResource("available")
                .addMethod("GET", new LambdaIntegration(getAvailableSessionsFunction));

        // GET /sessions/search?keyword={keyword}
        Resource sessionsResource = api.getRoot().addResource("sessions");
        sessionsResource.addResource("search")
                .addMethod("GET", new LambdaIntegration(searchSessionsFunction));
    }

    /**
     * Helper method to create a Lambda function.
     *
     * @param id      The ID of the Lambda function.
     * @param handler The fully qualified handler class name.
     * @return The created Lambda function.
     */
    private Function createLambdaFunction(String id, String handler) {
        return Function.Builder.create(this, id)
                .runtime(Runtime.JAVA_11)
                .code(Code.fromAsset("target/session-0.1.jar")) // Path to the JAR file
                .handler(handler)
                .memorySize(512)
                .timeout(software.amazon.awscdk.Duration.seconds(30))
                .build();
    }
}