package io.antmedia.logger;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class AntmediaAppenderTest {

    private AntmediaAppender antmediaAppender;

    @Mock
    GoogleAnalyticsLogger googleAnalyticsLogger;

    @Before
    public void before(){
        antmediaAppender = Mockito.spy(new AntmediaAppender(googleAnalyticsLogger));
    }

    @Test
    public void logNotCalledWhenThrowbleProxyIsNull(){
        ILoggingEvent iLoggingEvent = Mockito.mock(ILoggingEvent.class);
        Mockito.when(iLoggingEvent.getThrowableProxy()).thenReturn(null);
        antmediaAppender.append(iLoggingEvent);
        Mockito.verify(googleAnalyticsLogger,Mockito.never()).log(Mockito.anyString());
    }

    @Test
    public void logCalledWhenThrowbleProxyIsNotNull(){
        ILoggingEvent iLoggingEvent = Mockito.mock(ILoggingEvent.class);
        Mockito.when(iLoggingEvent.getThrowableProxy()).thenReturn(Mockito.mock(IThrowableProxy.class));
        antmediaAppender.append(iLoggingEvent);
        Mockito.verify(googleAnalyticsLogger).log(Mockito.anyString());
    }
}
