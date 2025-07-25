package com.webapplication.PoliclinicoSagradoCorazon.dao;

import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.webapplication.PoliclinicoSagradoCorazon.model.Pago;

import com.webapplication.PoliclinicoSagradoCorazon.repository.PagoComandoRepository;

@Repository
public class PagoDAOImpl implements PagoComandoRepository {
    private final JdbcTemplate jdbc;

    public PagoDAOImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public int insertarPago(Pago pago) {
        String sql = "INSERT INTO pago (metodo, monto, estado) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, pago.getMetodo());
            ps.setBigDecimal(2, pago.getMonto());
            ps.setString(3, pago.getEstado());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("No se generó un ID para el pago.");
        }
        return key.intValue();
    }
}
