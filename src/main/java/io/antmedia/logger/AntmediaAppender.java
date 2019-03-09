package io.antmedia.logger;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.AppenderBase;

/**
 * Appender for logback in charge of sending the logged events to a Google Analytics server.
 */
public class AntmediaAppender extends AppenderBase<ILoggingEvent> {
    private final GoogleAnalyticsLogger googleAnalyticsLogger;

    public AntmediaAppender() {
        googleAnalyticsLogger = new GoogleAnalyticsLoggerImp();
    }

    public AntmediaAppender(GoogleAnalyticsLogger googleAnalyticsLogger) {
        this.googleAnalyticsLogger = googleAnalyticsLogger;
    }

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        if (LoggerEnvironment.isManagingThread()) {
            return;
        }

        LoggerEnvironment.startManagingThread();
        try {

            IThrowableProxy throwbleProxy = iLoggingEvent.getThrowableProxy();
            if (throwbleProxy != null) {
                String throwableStr = ThrowableProxyUtil.asString(throwbleProxy);
                googleAnalyticsLogger.log(throwableStr);
            }
        } catch (Exception e) {
            addError("An exception occurred", e);
        } finally {
            LoggerEnvironment.stopManagingThread();
        }
    }

    @Override
    public void stop() {
        LoggerEnvironment.startManagingThread();
        try {
            if (!isStarted()) {
                return;
            }
            super.stop();
        } catch (Exception e) {
            addError("An exception occurred", e);
        } finally {
            LoggerEnvironment.stopManagingThread();
        }
    }
}