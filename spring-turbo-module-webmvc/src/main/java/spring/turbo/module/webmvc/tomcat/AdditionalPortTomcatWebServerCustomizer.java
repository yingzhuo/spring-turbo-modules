package spring.turbo.module.webmvc.tomcat;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

/**
 * @author 应卓
 * @since 3.3.1
 */
public class AdditionalPortTomcatWebServerCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    private int port = 8080;
    private String protocol = TomcatServletWebServerFactory.DEFAULT_PROTOCOL;

    /**
     * 默认构造方法
     */
    public AdditionalPortTomcatWebServerCustomizer() {
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addAdditionalTomcatConnectors(createStanderConnector());
    }

    private Connector createStanderConnector() {
        var connector = new Connector(protocol);
        connector.setPort(port);
        return connector;
    }

}
