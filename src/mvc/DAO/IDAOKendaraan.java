/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mvc.DAO;

import java.util.List;
import mvc.Model.Kendaraan;

/**
 *
 * @author ACER
 */
public interface IDAOKendaraan {
    public void insert(Kendaraan k);
    public void update(Kendaraan k);
    public void delete(int id, String jenis); 
    public List<Kendaraan> getAll();
    public List<Kendaraan> getCari(String keyword);
}
