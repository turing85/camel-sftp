package de.turing85.camel.sftp;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;
import org.apache.camel.main.MainConfigurationProperties;

import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.sftp;
import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.timer;

public class CamelSftp {
  public static void main(String... args) throws Exception {
    Main main = new Main();
    try (MainConfigurationProperties configure = main.configure()) {
      configure.addRoutesBuilder(sftpRoute());
      main.run();
    }
  }

  private static RouteBuilder sftpRoute() {
    final String password = "wrong&foo";
    return new RouteBuilder() {
      @Override
      public void configure() {
        // @formatter:off
        from(
            timer("write-timer")
                .fixedRate(true)
                .period(1000))
            .routeId("writer")
            .setHeader("CamelFileName", simple("upload/file-${date:now:yyyyMMdd-HHmmss}.txt"))
            .setBody(constant("Hello"))
            .to(
                sftp("localhost:10022")
                    .username("foo")
                    .password(password))
            .log("File ${file:name} written");
        // @formatter:on
      }
    };
  }
}
