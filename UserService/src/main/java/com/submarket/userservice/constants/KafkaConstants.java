package com.submarket.userservice.constants;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KafkaConstants {
    public final static String TOPIC_NAME_SUB = "sub";
    public final static String TOPIC_NAME_CANCEL_SUB = "sub-cancel";

    public final static String TOPIC_NAME_ITEM_LIKED = "item-liked";

    public final static String TOPIC_NAME_CANCEL_ITEM_LIKED = "item-liked-cancel";
}
