
package com.fitlife.membresia.repository;


import com.fitlife.membresia.model.membresiamodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface membresiarepository extends JpaRepository<membresiamodel, Long> {
}
