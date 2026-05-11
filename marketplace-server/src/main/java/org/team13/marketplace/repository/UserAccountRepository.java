package org.team13.marketplace.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.team13.marketplace.model.UserAccount;

public interface UserAccountRepository extends MongoRepository<UserAccount, String> {
}
