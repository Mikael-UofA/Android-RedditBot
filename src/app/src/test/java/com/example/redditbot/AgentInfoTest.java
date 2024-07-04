package com.example.redditbot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.example.redditbot.DataHolders.AgentInfo;

import org.junit.jupiter.api.Test;

public class AgentInfoTest {
    @Test
    void testNoArgConstructor() {
        AgentInfo agentInfo = new AgentInfo();
        assertNull(agentInfo.getAgentClientId());
        assertNull(agentInfo.getAgentClientSecret());
        assertNull(agentInfo.getAgentAppName());
        assertNull(agentInfo.getAgentAuthorName());
    }

    @Test
    void testParameterizedConstructor() {
        String clientId = "client-id";
        String clientSecret = "client-secret";
        String appName = "app-name";
        String authorName = "author-name";

        AgentInfo agentInfo = new AgentInfo(clientId, clientSecret, appName, authorName);

        assertEquals(clientId, agentInfo.getAgentClientId());
        assertEquals(clientSecret, agentInfo.getAgentClientSecret());
        assertEquals(appName, agentInfo.getAgentAppName());
        assertEquals(authorName, agentInfo.getAgentAuthorName());
    }

    @Test
    void testGetters() {
        String clientId = "client-id";
        String clientSecret = "client-secret";
        String appName = "app-name";
        String authorName = "author-name";

        AgentInfo agentInfo = new AgentInfo(clientId, clientSecret, appName, authorName);

        assertEquals(clientId, agentInfo.getAgentClientId());
        assertEquals(clientSecret, agentInfo.getAgentClientSecret());
        assertEquals(appName, agentInfo.getAgentAppName());
        assertEquals(authorName, agentInfo.getAgentAuthorName());
    }

    @Test
    void testSetters() {
        AgentInfo agentInfo = new AgentInfo();

        String clientId = "client-id";
        String clientSecret = "client-secret";
        String appName = "app-name";
        String authorName = "author-name";

        agentInfo.setAgentClientId(clientId);
        agentInfo.setAgentClientSecret(clientSecret);
        agentInfo.setAgentAppName(appName);
        agentInfo.setAgentAuthorName(authorName);

        assertEquals(clientId, agentInfo.getAgentClientId());
        assertEquals(clientSecret, agentInfo.getAgentClientSecret());
        assertEquals(appName, agentInfo.getAgentAppName());
        assertEquals(authorName, agentInfo.getAgentAuthorName());
    }
}
