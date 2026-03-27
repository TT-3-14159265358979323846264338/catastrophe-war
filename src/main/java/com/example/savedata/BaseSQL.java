package com.example.savedata;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseSQL{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "INT AUTO_INCREMENT", updatable = false)
	@Setter(AccessLevel.NONE)
	private Integer id;
	
	@Column(name = "number", columnDefinition = "INT UNSIGNED", nullable = false)
	private Integer number;
	
	public void setNumber(Integer number) {
		if(maxNumber() < number) {
			this.number = maxNumber();
			return;
		}
		this.number = number;
	}
	
	protected abstract Integer maxNumber();
}