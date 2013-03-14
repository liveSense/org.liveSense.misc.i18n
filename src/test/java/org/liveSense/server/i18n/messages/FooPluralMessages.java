package org.liveSense.server.i18n.messages;

import com.google.gwt.i18n.client.Messages;


public interface FooPluralMessages extends Messages {

    @Key("test_simple_int")
    String method_test_simple_int(@PluralCount int i);

//  @PluralText({"none", "aucun", "one", "un seul", "two", "une pair"})

}
