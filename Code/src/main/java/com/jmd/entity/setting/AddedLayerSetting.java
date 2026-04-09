package com.jmd.entity.setting;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

@Data
public class AddedLayerSetting implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private String name;
	private String url;
	private String type;

}
