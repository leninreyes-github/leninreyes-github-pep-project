package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account addAccount(Account account){
        String userNameAccount;
        String passwordAccount;
        Account accountTemp = new Account();
        userNameAccount = account.getUsername();
        passwordAccount = account.getPassword();
        accountTemp = accountDAO.getAccountByUserNameAndPassword(userNameAccount,null);
        if(userNameAccount.length()>0&&accountTemp==null&&passwordAccount.length()>=4){
            return accountDAO.insertAccount(account);
        }    
        return null;
    }

    public Account verifyAccount(Account account){
        String userNameAccount;
        String passwordAccount;
        userNameAccount = account.getUsername();
        passwordAccount = account.getPassword();
        return accountDAO.getAccountByUserNameAndPassword(userNameAccount,passwordAccount);
    }

    public Account verifyAccountById(int id){
        return accountDAO.getAccountById(id);
    }


}
