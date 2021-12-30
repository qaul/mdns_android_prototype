package com.github.druk.dnssd;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.after;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@Ignore
@PrepareForTest({DNSSDEmbedded.class})
public class DNSSDEmbeddedTest {

    private static final int TIMEOUT = 5000;

    DNSSDEmbedded mDNSSDEmbedded;

    @Before
    public void setup() {
        Context context = mock(Context.class);
        mDNSSDEmbedded = new DNSSDEmbedded(context, TIMEOUT);
        mockStatic(DNSSDEmbedded.class);
    }

    @Test
    public void init() throws InterruptedException {
        mDNSSDEmbedded.init();
        PowerMockito.verifyStatic(DNSSDEmbedded.class);
        DNSSDEmbedded.nativeInit();
        PowerMockito.verifyStatic(DNSSDEmbedded.class, timeout(TIMEOUT));
        DNSSDEmbedded.nativeLoop();
    }

    @Test
    public void initWithError() throws InterruptedException {
        PowerMockito.when(DNSSDEmbedded.nativeInit()).thenReturn(-1);
        mDNSSDEmbedded.init();
        PowerMockito.verifyStatic(DNSSDEmbedded.class);
        DNSSDEmbedded.nativeInit();
        PowerMockito.verifyStatic(DNSSDEmbedded.class, after(TIMEOUT).never());
        DNSSDEmbedded.nativeLoop();
    }

    @Test
    public void exit() throws InterruptedException {
        mDNSSDEmbedded.exit();
        PowerMockito.verifyStatic(DNSSDEmbedded.class, timeout(2 * TIMEOUT));
        DNSSDEmbedded.nativeExit();
    }

    @Test
    public void cancelStopTimer() throws InterruptedException {
        mDNSSDEmbedded.init();
        PowerMockito.verifyStatic(DNSSDEmbedded.class);
        DNSSDEmbedded.nativeInit();
        PowerMockito.verifyStatic(DNSSDEmbedded.class, timeout(TIMEOUT));
        DNSSDEmbedded.nativeLoop();
        mDNSSDEmbedded.exit();
        Thread.sleep(TIMEOUT / 2);
        mDNSSDEmbedded.init();
        Thread.sleep(TIMEOUT / 2);
        PowerMockito.verifyStatic(DNSSDEmbedded.class, never());
        DNSSDEmbedded.nativeExit();
        mDNSSDEmbedded.exit();
        PowerMockito.verifyStatic(DNSSDEmbedded.class, timeout(2 * TIMEOUT));
        DNSSDEmbedded.nativeExit();
    }

    @After
    public void tearDown() {
    }
}
