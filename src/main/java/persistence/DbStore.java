package persistence;

import model.Account;

public interface DbStore {
    Long addAccount(Account account);
}
