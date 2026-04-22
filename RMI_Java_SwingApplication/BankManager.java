import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BankManager extends Remote {
    void creerCompte(String id, double somme, String username) throws RemoteException;
    void ajouter(String id, double somme) throws RemoteException;
    void retirer(String id, double somme) throws RemoteException;
    double consulterSolde(String id) throws RemoteException;
    void transfererSolde(String id_C, String id_D, double somme) throws RemoteException;
    boolean authenticateUser(String username, String password) throws RemoteException;
    void registerUser(String username, String fullname, String password) throws RemoteException;
    String getFullname(String username) throws RemoteException;
    boolean userHasAccount(String username) throws RemoteException;
    String getAccountIdByUsername(String username) throws RemoteException;
}
