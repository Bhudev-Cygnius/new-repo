package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.StackProps;
import com.myorg.cdk.SessionStack;

public class SessionApp {
    public static void main(final String[] args) {
        // Create the CDK app
        App app = new App();

        // Create the SessionStack with required arguments
        new SessionStack(app, "SessionStack", StackProps.builder().build());

        // Synthesize the CDK app
        app.synth();
    }
}