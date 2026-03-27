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
@Table(name = "all_composition")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompositionSQL {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "INT AUTO_INCREMENT", updatable = false)
	@Setter(AccessLevel.NONE)
	private Integer id;
	
	@Column(length = 20, nullable = false)
	private String name;
	
	@Column(name = "right_weapon_0", columnDefinition = "INT", nullable = false)
	private Integer rightWeapon0;
	@Column(name = "center_core_0", columnDefinition = "INT UNSIGNED", nullable = false)
	private Integer centerCore0;
	@Column(name = "left_weapon_0", columnDefinition = "INT", nullable = false)
	private Integer leftWeapon0;
	
	@Column(name = "right_weapon_1", columnDefinition = "INT", nullable = false)
	private Integer rightWeapon1;
	@Column(name = "center_core_1", columnDefinition = "INT UNSIGNED", nullable = false)
	private Integer centerCore1;
	@Column(name = "left_weapon_1", columnDefinition = "INT", nullable = false)
	private Integer leftWeapon1;
	
	@Column(name = "right_weapon_2", columnDefinition = "INT", nullable = false)
	private Integer rightWeapon2;
	@Column(name = "center_core_2", columnDefinition = "INT UNSIGNED", nullable = false)
	private Integer centerCore2;
	@Column(name = "left_weapon_2", columnDefinition = "INT", nullable = false)
	private Integer leftWeapon2;
	
	@Column(name = "right_weapon_3", columnDefinition = "INT", nullable = false)
	private Integer rightWeapon3;
	@Column(name = "center_core_3", columnDefinition = "INT UNSIGNED", nullable = false)
	private Integer centerCore3;
	@Column(name = "left_weapon_3", columnDefinition = "INT", nullable = false)
	private Integer leftWeapon3;
	
	@Column(name = "right_weapon_4", columnDefinition = "INT", nullable = false)
	private Integer rightWeapon4;
	@Column(name = "center_core_4", columnDefinition = "INT UNSIGNED", nullable = false)
	private Integer centerCore4;
	@Column(name = "left_weapon_4", columnDefinition = "INT", nullable = false)
	private Integer leftWeapon4;
	
	@Column(name = "right_weapon_5", columnDefinition = "INT", nullable = false)
	private Integer rightWeapon5;
	@Column(name = "center_core_5", columnDefinition = "INT UNSIGNED", nullable = false)
	private Integer centerCore5;
	@Column(name = "left_weapon_5", columnDefinition = "INT", nullable = false)
	private Integer leftWeapon5;
	
	@Column(name = "right_weapon_6", columnDefinition = "INT", nullable = false)
	private Integer rightWeapon6;
	@Column(name = "center_core_6", columnDefinition = "INT UNSIGNED", nullable = false)
	private Integer centerCore6;
	@Column(name = "left_weapon_6", columnDefinition = "INT", nullable = false)
	private Integer leftWeapon6;
	
	@Column(name = "right_weapon_7", columnDefinition = "INT", nullable = false)
	private Integer rightWeapon7;
	@Column(name = "center_core_7", columnDefinition = "INT UNSIGNED", nullable = false)
	private Integer centerCore7;
	@Column(name = "left_weapon_7", columnDefinition = "INT", nullable = false)
	private Integer leftWeapon7;
	
	@Transient
	private final String RIGHT = "rightWeapon";
	@Transient
	private final String CENTER = "centerCore";
	@Transient
	private final String LEFT = "leftWeapon";
	@Transient
	private final int NO_WEAPON = -1;
	@Transient
	private final int NO_CORE = 0;
	
	public void initialize() {
		for(int i = 0; i < 8; i++) {
			setRightWeapon(i, NO_WEAPON);
			setCenterCore(i, NO_CORE);
			setLeftWeapon(i, NO_WEAPON);
		}
	}
	
	public void setRightWeapon(int compositionId, int newId) {
		setData(RIGHT, compositionId, newId);
	}
	
	public void setCenterCore(int compositionId, int newId) {
		setData(CENTER, compositionId, newId);
	}
	
	public void setLeftWeapon(int compositionId, int newId) {
		setData(LEFT, compositionId, newId);
	}
	
	void setData(String code, int compositionId, int newId) {
		try {
			Field field = this.getClass().getDeclaredField(code + compositionId);
			field.setAccessible(true);
			field.set(this, newId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}