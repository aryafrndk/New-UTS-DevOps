package DAOInterface;

import java.util.List;
import model.TambahData;

/**
 *
 * @author ASUS
 */
public interface IDAOData {
    public List<TambahData> getAll();
    public void insert(TambahData b);
    public void update(TambahData b);
    public void delete(String nim);
    public List<TambahData> search(String keyword); // Tambahkan method ini
}