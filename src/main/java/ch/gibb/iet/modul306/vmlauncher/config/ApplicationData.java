package ch.gibb.iet.modul306.vmlauncher.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationData {
	@Value("${info.project.group.id}")
	private String groupId;

	@Value("${info.project.artifact.id}")
	private String artifactId;

	@Value("${info.build.name}")

	private String buildName;
	@Value("${info.build.version}")

	private String buildVersion;
	@Value("${info.build.description}")

	private String buildDescription;

	public ApplicationData() {
		super();
	}

	public String getGroupId() {
		return this.groupId;
	}

	public String getArtifactId() {
		return this.artifactId;
	}

	public String getBuildName() {
		return this.buildName;
	}

	public String getBuildVersion() {
		return this.buildVersion;
	}

	public String getBuildDescription() {
		return this.buildDescription;
	}
}