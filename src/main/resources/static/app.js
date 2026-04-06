const API_BASE = "/api";
const REFRESH_INTERVAL_MS = 3000;

const state = {
    partners: [],
    contacts: []
};

const elements = {
    partnerCount: document.querySelector("#partner-count"),
    contactCount: document.querySelector("#contact-count"),
    lastSync: document.querySelector("#last-sync"),
    partnersTableBody: document.querySelector("#partners-table-body"),
    contactsTableBody: document.querySelector("#contacts-table-body"),
    messageBox: document.querySelector("#message-box")
};

document.addEventListener("DOMContentLoaded", async () => {
    await refreshDashboard();
    window.setInterval(refreshDashboard, REFRESH_INTERVAL_MS);
    document.addEventListener("visibilitychange", async () => {
        if (document.visibilityState === "visible") {
            await refreshDashboard();
        }
    });
});

async function refreshDashboard() {
    try {
        const partnerList = await apiFetch("/partners");
        const partnerDetails = await Promise.all(
            partnerList.map((partner) => apiFetch(`/partners/${partner.id}`))
        );

        state.partners = partnerList;
        state.contacts = partnerDetails.flatMap((partner) =>
            (partner.contacts ?? []).map((contact) => ({
                partnerName: partner.name,
                ...contact
            }))
        );

        renderPartners();
        renderContacts();
        renderStats();
        clearMessage();
    } catch (error) {
        showMessage(error.message);
    }
}

function renderPartners() {
    const rows = state.partners.map((partner) => {
        const statusClass = partner.partnerStatus?.toLowerCase() ?? "";

        return `
            <tr>
                <td>${escapeHtml(partner.name)}</td>
                <td><span class="status-pill ${statusClass}">${escapeHtml(partner.partnerStatus ?? "NEW")}</span></td>
                <td>${escapeHtml(shortId(partner.id))}</td>
            </tr>
        `;
    }).join("");

    elements.partnersTableBody.innerHTML = rows || `<tr><td colspan="3" class="empty-state">No partners yet.</td></tr>`;
}

function renderContacts() {
    const rows = state.contacts.map((contact) => `
        <tr>
            <td>${escapeHtml(contact.partnerName)}</td>
            <td>${escapeHtml(contact.firstName)} ${escapeHtml(contact.lastName)}</td>
            <td>${escapeHtml(contact.position ?? "-")}</td>
            <td>${escapeHtml(contact.email ?? "-")}</td>
            <td>${escapeHtml(formatPhone(contact))}</td>
            <td>${contact.primary ? "Yes" : "No"}</td>
        </tr>
    `).join("");

    elements.contactsTableBody.innerHTML = rows || `<tr><td colspan="6" class="empty-state">No contacts yet.</td></tr>`;
}

function renderStats() {
    elements.partnerCount.textContent = String(state.partners.length);
    elements.contactCount.textContent = String(state.contacts.length);
    elements.lastSync.textContent = new Date().toLocaleTimeString("cs-CZ", {
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit"
    });
}

async function apiFetch(path, options = {}) {
    const response = await fetch(`${API_BASE}${path}`, {
        headers: {
            "Content-Type": "application/json",
            ...(options.headers ?? {})
        },
        ...options
    });

    if (response.status === 204) {
        return null;
    }

    const text = await response.text();
    const data = text ? JSON.parse(text) : null;

    if (!response.ok) {
        throw new Error(data?.message ?? `Request failed with status ${response.status}.`);
    }

    return data;
}

function formatPhone(contact) {
    const prefix = (contact.countryCallingCode ?? "").trim();
    const number = (contact.phoneNumber ?? "").replace(/\s+/g, "");

    if (!prefix && !number) {
        return "-";
    }

    const groupedNumber = number
        ? number.match(/.{1,3}/g)?.join(" ") ?? number
        : "";

    return [prefix, groupedNumber].filter(Boolean).join(" ");
}

function shortId(value) {
    return value ? `${value.slice(0, 8)}...` : "-";
}

function showMessage(message) {
    elements.messageBox.textContent = message;
    elements.messageBox.classList.remove("hidden");
}

function clearMessage() {
    elements.messageBox.textContent = "";
    elements.messageBox.classList.add("hidden");
}

function escapeHtml(value) {
    return String(value ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll("\"", "&quot;")
        .replaceAll("'", "&#39;");
}
