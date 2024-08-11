package com.skillsmanagement.helper;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Skill levels available for a person.")
public enum SkillLevel {
	@Schema(description = "EXPERT")
	EXPERT,
	@Schema(description = "PRACTITIONER")
	PRACTITIONER,
	@Schema(description = "WORKING")
	WORKING,
	@Schema(description = "AWARENESS")
	AWARENESS;

	public static SkillLevel fromString(String str) {
		for (SkillLevel level : SkillLevel.values()) {
			if (level.name().equalsIgnoreCase(str)) {
				return level;
			}
		}
		throw new IllegalArgumentException("No enum constant for value: " + str);
	}

	public static SkillLevel convertToSkillLevel(String value) {
		try {
			return SkillLevel.valueOf(value.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid skill level: " + value, e);
		}
	}
}

