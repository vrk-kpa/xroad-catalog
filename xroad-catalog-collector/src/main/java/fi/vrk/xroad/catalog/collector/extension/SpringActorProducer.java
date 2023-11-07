/**
 * The MIT License
 *
 * Copyright (c) 2023- Nordic Institute for Interoperability Solutions (NIIS)
 * Copyright (c) 2016-2023 Finnish Digital Agency
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fi.vrk.xroad.catalog.collector.extension;

import akka.actor.Actor;
import akka.actor.IndirectActorProducer;
import org.springframework.context.ApplicationContext;

public class SpringActorProducer implements IndirectActorProducer {

    private final ApplicationContext applicationContext;
    private final String actorBeanName;

    // arguments to use when creating a bean instance using explicit arguments
    private final Object[] args;

    public SpringActorProducer(ApplicationContext applicationContext,
        String actorBeanName) {
        this.applicationContext = applicationContext;
        this.actorBeanName = actorBeanName;
        args = null;
    }

    public SpringActorProducer(ApplicationContext applicationContext,
                               String actorBeanName,
                               Object... args) {
        this.applicationContext = applicationContext;
        this.actorBeanName = actorBeanName;
        this.args = args;
    }


    @Override
    public Actor produce() {
        if (args != null) {
            return (Actor) applicationContext.getBean(actorBeanName, args);
        } else {
            return (Actor) applicationContext.getBean(actorBeanName);
        }
    }

    @Override
    public Class<? extends Actor> actorClass() {
        return (Class<? extends Actor>) applicationContext.getType(actorBeanName);
    }
}