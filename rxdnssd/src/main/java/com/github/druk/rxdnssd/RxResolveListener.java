/*
 * Copyright (C) 2016 Andriy Druk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.druk.rxdnssd;

import com.github.druk.dnssd.DNSSDService;
import com.github.druk.dnssd.ResolveListener;

import java.util.Map;

import rx.Subscriber;

class RxResolveListener implements ResolveListener {
    private final Subscriber<? super BonjourService> subscriber;
    private final BonjourService.Builder builder;

    RxResolveListener(Subscriber<? super BonjourService> subscriber, BonjourService service) {
        this.subscriber = subscriber;
        this.builder = new BonjourService.Builder(service);
    }

    @Override
    public void serviceResolved(DNSSDService resolver, int flags, int ifIndex, String fullName, String hostName, int port,  Map<String, String> txtRecord) {
        if (subscriber.isUnsubscribed()) {
            return;
        }
        BonjourService bonjourService = builder.port(port).hostname(hostName).dnsRecords(txtRecord).build();
        subscriber.onNext(bonjourService);
        subscriber.onCompleted();
    }

    @Override
    public void operationFailed(DNSSDService service, int errorCode) {
        if (subscriber.isUnsubscribed()) {
            return;
        }
        subscriber.onError(new RuntimeException("DNSSD resolve error: " + errorCode));
    }
}
