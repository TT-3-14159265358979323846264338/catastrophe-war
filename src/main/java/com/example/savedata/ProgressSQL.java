package com.example.savedata;

import java.lang.reflect.Field;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "stage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressSQL {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "INT AUTO_INCREMENT", updatable = false)
	@Setter(AccessLevel.NONE)
	private Integer id;
	
	@Column(name = "stage_clear", nullable = false)
	private Boolean stageClear;
	
	@Column(name = "merit_clear_0", nullable = false)
	private Boolean meritClear0;
	@Column(name = "merit_clear_1", nullable = false)
	private Boolean meritClear1;
	@Column(name = "merit_clear_2", nullable = false)
	private Boolean meritClear2;
	@Column(name = "merit_clear_3", nullable = false)
	private Boolean meritClear3;
	@Column(name = "merit_clear_4", nullable = false)
	private Boolean meritClear4;
	@Column(name = "merit_clear_5", nullable = false)
	private Boolean meritClear5;
	@Column(name = "merit_clear_6", nullable = false)
	private Boolean meritClear6;
	@Column(name = "merit_clear_7", nullable = false)
	private Boolean meritClear7;
	@Column(name = "merit_clear_8", nullable = false)
	private Boolean meritClear8;
	@Column(name = "merit_clear_9", nullable = false)
	private Boolean meritClear9;
	
	@Transient
	private final String CLEAR = "meritClear";
	@Transient
	private final int COUNT = 10;
	
	public void initialize() {
		stageClear = false;
		for(int i = 0; i < COUNT; i++) {
			setData(i, false);
		}
	}
	
	public void setData(int clearId, boolean hasCleared) {
		try {
			Field field = this.getClass().getDeclaredField(CLEAR + clearId);
			field.setAccessible(true);
			field.set(this, hasCleared);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}