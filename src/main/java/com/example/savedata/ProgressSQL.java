package com.example.savedata;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.IntStream;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import com.example.defaultdata.DefaultEnum;
import com.example.defaultdata.Stage;

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
	private static final String CLEAR = "meritClear";
	
	@Transient
	private static final int COUNT = 10;
	
	@Transient
	private static final List<Field> MERIT_FIELD;
	
	@Transient
	private int ACTIVE_NUMBER;
	
	static {
		MERIT_FIELD = IntStream.range(0, COUNT).mapToObj(i -> {
			try {
				Field field = ProgressSQL.class.getDeclaredField(CLEAR + i);
				field.setAccessible(true);
				return field;
			}catch (Exception e) {
				throw new ExceptionInInitializerError(e);
			}
		}).toList();
	}
	
	@PostLoad
	void activeNumber() {
		ACTIVE_NUMBER = DefaultEnum.getEnum(Stage.values(), id - 1).getLabel().getMerit().size();
	}
	
	public void initialize() {
		stageClear = false;
		for(int i = 0; i < COUNT; i++) {
			setData(i, false);
		}
		activeNumber();
	}
	
	public List<Boolean> getMerit(){
		return IntStream.range(0, COUNT).mapToObj(this::merit).limit(ACTIVE_NUMBER).toList();
	}
	
	boolean merit(int meritId) {
		try {
			return (Boolean) MERIT_FIELD.get(meritId).get(this);
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void setData(List<Boolean> merit) {
		IntStream.range(0, merit.size()).forEach(i -> setData(i, merit.get(i)));
	}
	
	public void setData(int meritId, boolean hasCleared) {
		try {
			MERIT_FIELD.get(meritId).set(this, hasCleared);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}