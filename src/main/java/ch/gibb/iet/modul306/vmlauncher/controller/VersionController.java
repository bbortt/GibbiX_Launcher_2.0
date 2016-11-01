package ch.gibb.iet.modul306.vmlauncher.controller;

import org.springframework.beans.factory.annotation.Value;

public class VersionController extends AbstractController {
	public VersionController() {
		super();
	}

	@Value("${info.project.model.version}")
	private String modelVersion;

	@Value("${info.project.group.id}")
	private String groupId;

	@Value("${info.project.artifact.id}")
	private String artifactId;

	@Value("${info.build.name}")
	private String buildName;

	@Value("${info.build.version}")
	private String buildVersion;

	public String getModelVersion() {
		return modelVersion;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public String getBuildName() {
		return buildName;
	}

	public String getBuildVersion() {
		return buildVersion;
	}
}
