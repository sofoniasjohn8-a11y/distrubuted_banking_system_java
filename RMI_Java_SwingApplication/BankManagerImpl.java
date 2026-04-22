import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class BankManagerImpl extends UnicastRemoteObject implements BankManager {
    private final DatabaseManager dbManager;

    protected BankManagerImpl() throws RemoteException {
        this.dbManager = DatabaseManager.getInstance();
    }

    @Override
    public void creerCompte(String id, double somme, String username) throws RemoteException {
        try {
            dbManager.createAccount(id, somme, username);
        } catch (Exception e) {
            throw new RemoteException("Error creating account: " + e.getMessage());
        }
    }

    @Override
    public void ajouter(String id, double somme) throws RemoteException {
        try {
            dbManager.deposit(id, somme);
        } catch (Exception e) {
            throw new RemoteException("Error depositing funds: " + e.getMessage());
        }
    }

    @Override
    public void retirer(String id, double somme) throws RemoteException {
        try {
            dbManager.withdraw(id, somme);
        } catch (Exception e) {
            throw new RemoteException("Error withdrawing funds: " + e.getMessage());
        }
    }

    @Override
    public double consulterSolde(String id) throws RemoteException {
        try {
            return dbManager.getBalance(id);
        } catch (Exception e) {
            throw new RemoteException("Error retrieving balance: " + e.getMessage());
        }
    }

    @Override
    public void transfererSolde(String id_C, String id_D, double somme) throws RemoteException {
        try {
            dbManager.transfer(id_C, id_D, somme);
        } catch (Exception e) {
            throw new RemoteException("Error transferring funds: " + e.getMessage());
        }
    }

    @Override
    public boolean authenticateUser(String username, String password) throws RemoteException {
        return dbManager.authenticateUser(username, password);
    }

    @Override
    public void registerUser(String username, String fullname, String password) throws RemoteException {
        try {
            dbManager.registerUser(username, fullname, password);
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public String getFullname(String username) throws RemoteException {
        return dbManager.getFullname(username);
    }

    @Override
    public boolean userHasAccount(String username) throws RemoteException {
        return dbManager.userHasAccount(username);
    }

    @Override
    public String getAccountIdByUsername(String username) throws RemoteException {
        return dbManager.getAccountIdByUsername(username);
    }
}
