package com.example.savedata;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "select_target")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectSQL {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "INT AUTO_INCREMENT", updatable = false)
	@Setter(AccessLevel.NONE)
	private Integer id;
	
	@Column(name = "select_code", columnDefinition = "INT", nullable = false)
	private Integer selectCode;
}