package telran.accounting.domain.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.accounting.entity.UserAccount;

public interface UserAccountsRepository extends MongoRepository<UserAccount, String> {

}
