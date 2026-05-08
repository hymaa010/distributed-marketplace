package org.team13.marketplace.dto;

import lombok.Builder;
import lombok.Data;
import org.team13.marketplace.model.Item;

import java.util.List;

@Data
@Builder
public class AccountInfoResponse {

    private String userId;
    private String username;
    private String email;
    private double balance;
    // Items this user currently has
    private List<Item> ownedItems;

    // Items this user bought from others
    private List<PurchasedItem> purchasedItems;

    // Items this user has successfully sold
    private List<PurchasedItem> soldItems;
}
