package spring.turbo.module.jwt.env;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.diagnostics.FailureAnalyzer;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.lang.Nullable;

import static spring.turbo.util.ClassUtils.isPresent;

/**
 * 依赖检查工具
 *
 * @author 应卓
 * @since 3.1.1
 */
class DependenciesCheckingComponent implements EnvironmentPostProcessor, FailureAnalyzer {

    private static final boolean PRESENT_HUTOOL = isPresent("cn.hutool.jwt.signers.JWTSigner");
    private static final boolean PRESENT_JAVA_JWT = isPresent("com.auth0.jwt.algorithms.Algorithm");

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

        if (!(PRESENT_HUTOOL || PRESENT_JAVA_JWT)) {
            throw new BadDependencies();
        }
    }

    @Nullable
    @Override
    public FailureAnalysis analyze(Throwable failure) {
        if (failure instanceof BadDependencies) {
            var desc = """
                    Make sure at least one of library introduced to the classpath.
                    1. "com.auth0:java-jwt:<version>"
                    2. "cn.hutool:hutool-jwt:<version>"
                    """;

            var action = "Re-check your Dependencies";

            return new FailureAnalysis(
                    desc,
                    action,
                    failure
            );
        }

        return null;
    }

    private static class BadDependencies extends RuntimeException {
    }

}
