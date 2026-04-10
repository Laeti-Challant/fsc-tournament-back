package com.filsanguinaire.tournament.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.filsanguinaire.tournament.bll.IRulesService;
import com.filsanguinaire.tournament.bo.AllowedInducement;
import com.filsanguinaire.tournament.bo.Event;
import com.filsanguinaire.tournament.bo.EventStatus;
import com.filsanguinaire.tournament.bo.Role;
import com.filsanguinaire.tournament.bo.RosterCategory;
import com.filsanguinaire.tournament.bo.TournamentRules;
import com.filsanguinaire.tournament.bo.User;
import com.filsanguinaire.tournament.dal.EventRepository;
import com.filsanguinaire.tournament.dal.TournamentRulesRepository;
import com.filsanguinaire.tournament.dal.UserRepository;
import com.filsanguinaire.tournament.dto.rules.TournamentRulesDTO;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("local") 
public class TestDatas {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TournamentRulesRepository rulesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // =========================================================================
    // TEST 01 — USERS
    // =========================================================================

    @Test
    void test01_InsertUsers() {
        final User admin = userRepository.save(User.builder()
                .email("admin@filsanguinaire.fr")
                .pseudo("Admin_FSC")
                .passwordHash(passwordEncoder.encode("AdminTest1!"))
                .role(Role.ADMIN)
                .active(true)
                .build());

        final User organisateur = userRepository.save(User.builder()
                .email("organisateur@filsanguinaire.fr")
                .pseudo("Orga_FSC")
                .passwordHash(passwordEncoder.encode("OrgaTest1!"))
                .role(Role.ADMIN)
                .active(true)
                .build());

        final User player1 = userRepository.save(User.builder()
                .email("coach1@bloodbowl.fr")
                .pseudo("KroakMaster")
                .passwordHash(passwordEncoder.encode("PlayerTest1!"))
                .role(Role.PLAYER)
                .active(true)
                .build());

        final User player2 = userRepository.save(User.builder()
                .email("coach2@bloodbowl.fr")
                .pseudo("NurgleFan85")
                .passwordHash(passwordEncoder.encode("PlayerTest1!"))
                .role(Role.PLAYER)
                .active(true)
                .build());

        final User player3 = userRepository.save(User.builder()
                .email("coach3@bloodbowl.fr")
                .pseudo("HalflingHero")
                .passwordHash(passwordEncoder.encode("PlayerTest1!"))
                .role(Role.PLAYER)
                .active(true)
                .build());

        assertThat(admin).isNotNull();
        assertThat(organisateur).isNotNull();
        assertThat(player1).isNotNull();
        assertThat(player2).isNotNull();
        assertThat(player3).isNotNull();
    }

    // =========================================================================
    // TEST 02 — EVENT VIII (passé, FINISHED)
    // =========================================================================

    @Test
    void test02_InsertEventVIII() {
        final Event choletBowlVIII = eventRepository.save(Event.builder()
                .name("CholetBowl VIII")
                .eventDate(LocalDate.of(2025, 11, 22))
                .registrationDeadline(LocalDate.of(2025, 11, 1))
                .status(EventStatus.FINISHED)
                .maxParticipants(24)
                .nbRounds(3)
                .build());

        assertThat(choletBowlVIII).isNotNull();
        assertThat(choletBowlVIII.getId()).isNotNull();
    }

    // =========================================================================
    // TEST 03 — RULES DU CHOLETBOWL VIII (avec inducements et catégories)
    // =========================================================================

    @Test
    void test03_InsertRulesVIII() {
        final Event eventVIII = eventRepository.findAll().stream()
                .filter(e -> e.getName().equals("CholetBowl VIII"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Event CholetBowl VIII introuvable"));

        final TournamentRules rules = TournamentRules.builder()
                .event(eventVIII)
                .budgetPo(1_050_000)
                .pspPool((short) 30)
                .maxSkillsPerPlayer((short) 2)
                .resurrectionMode(true)
                .mogettePspValue((short) 5)
                .mogettePoValue(25_000)
                .notesText("""
                        Mode Résurrection : morts, blessés et XP gagnés lors d'un match
                        ne sont pas conservés pour le match suivant.
                        Règles non optionnelles BB2020 + dernières FAQ applicables.
                        Compétences aléatoires tirées à chaque match (temps inclus dans le match).
                        Rumbelow Ronanskin autorisé uniquement pour les Halflings.
                        """)
                .rosterText("""
                        Rosters autorisés : livre BB2020 + Teams of Legend + publications
                        jusqu'au 1er novembre 2025 + Slanns (document NAF).
                        Budget : 1 050 000 po — 11 joueurs minimum.
                        30 psp à répartir. Max 2 compétences par joueur.
                        Mogettes d'or selon la catégorie du roster
                        (1 mogette = 5 psp OU 25 000 po).
                        """)
                .build();

        // --- Coups de pouce (inducements) autorisés ---
        List.of(
                inducement("Cheerleaders Intérimaires",   (short) 0, (short) 4),
                inducement("Coachs Assistants à temps partiel", (short) 0, (short) 3),
                inducement("Mage météo",                  (short) 0, (short) 1),
                inducement("Fûts de Bloodweiser",         (short) 0, (short) 2),
                inducement("Entraînement supplémentaire", (short) 0, (short) 8),
                inducement("Pots de vin",                 (short) 0, (short) 3),
                inducement("Apothicaire Ambulant",        (short) 0, (short) 2),
                inducement("Assistant Funéraire",         (short) 0, (short) 1),
                inducement("Médecin de la Peste",         (short) 0, (short) 1),
                inducement("Cuistot Halfling",            (short) 0, (short) 1),
                inducement("Rookies déchaînés",           (short) 0, (short) 1),
                inducement("Arbitre partial",             (short) 0, (short) 1),
                inducement("Staff célèbre",               (short) 0, (short) 2),
                inducement("Géant",                       (short) 0, (short) 1)
        ).forEach(i -> {
            i.setRules(rules);
            rules.getAllowedInducements().add(i);
        });

        // --- Catégories de rosters ---
        // Catégorie 0 : les 3 rosters les plus représentés l'année passée
        List.of(
                category("Renégats du Chaos", (short) 0, false),
                category("Nains",             (short) 0, false),
                category("Nurgle",            (short) 0, false),

                // Catégorie 1
                category("Elfes Noirs",          (short) 1, false),
                category("Amazones",             (short) 1, false),
                category("Hommes-Lézards",       (short) 1, false),
                category("Morts-Vivants",        (short) 1, false),
                category("Orcs",                 (short) 1, false),
                category("Skavens",              (short) 1, false),

                // Catégorie 2
                category("Elfes Sylvains",           (short) 2, false),
                category("Bas-fonds",                (short) 2, false),
                category("Humains",                  (short) 2, false),
                category("Nordiques",                (short) 2, false),
                category("Horreurs Nécromantiques",  (short) 2, false),
                category("Nain du Chaos",            (short) 2, false),
                category("Vampires",                 (short) 2, false),

                // Catégorie 3
                category("Union Elfique",    (short) 3, false),
                category("Noblesse Impériale",(short) 3, false),
                category("Élus du Chaos",    (short) 3, false),
                category("Hauts-Elfes",      (short) 3, false),
                category("Orcs Noirs",       (short) 3, false),

                // Catégorie 4
                category("Alliance du Vieux Monde", (short) 4, false),
                category("Slanns",                  (short) 4, false),
                category("Khemris",                 (short) 4, false),

                // Catégorie 5
                category("Khornes",   (short) 5, false),
                category("Snotlings", (short) 5, false),
                category("Gnomes",    (short) 5, false),

                // Catégorie 6
                category("Halflings", (short) 6, false),
                category("Gobelins",  (short) 6, false),
                category("Ogres",     (short) 6, false)
        ).forEach(c -> {
            c.setRules(rules);
            rules.getRosterCategories().add(c);
        });

        final TournamentRules saved = rulesRepository.save(rules);

        assertThat(saved).isNotNull();
        assertThat(saved.getAllowedInducements()).hasSize(14);
        assertThat(saved.getRosterCategories()).hasSize(30);
    }

    // =========================================================================
    // TEST 04 — EVENT IX (à venir, PLANNED)
    // =========================================================================

    @Test
    void test04_InsertEventIX() {
        final Event choletBowlIX = eventRepository.save(Event.builder()
                .name("CholetBowl IX")
                .eventDate(LocalDate.of(2026, 11, 14))
                .registrationDeadline(LocalDate.of(2026, 10, 31))
                .status(EventStatus.PLANNED)
                .maxParticipants(24)
                .nbRounds(3)
                .build());

        assertThat(choletBowlIX).isNotNull();
        assertThat(choletBowlIX.getId()).isNotNull();
    }

    // =========================================================================
    // TEST 05 — RULES DU CHOLETBOWL IX (clone du VIII)
    // =========================================================================

    @Autowired
    private IRulesService rulesService;

    @Test
    void test05_CloneRulesForIX() {
        final Event eventVIII = eventRepository.findAll().stream()
                .filter(e -> e.getName().equals("CholetBowl VIII"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Event CholetBowl VIII introuvable"));

        final Event eventIX = eventRepository.findAll().stream()
                .filter(e -> e.getName().equals("CholetBowl IX"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Event CholetBowl IX introuvable"));

        final TournamentRulesDTO cloned = rulesService.cloneFromEvent(eventIX.getId(), eventVIII.getId());

        assertThat(cloned).isNotNull();
        assertThat(cloned.getAllowedInducements()).hasSize(14);
        assertThat(cloned.getRosterCategories()).hasSize(30);
    }

    // =========================================================================
    // HELPERS
    // =========================================================================

    private AllowedInducement inducement(String name, short min, short max) {
        return AllowedInducement.builder()
                .name(name)
                .minQty(min)
                .maxQty(max)
                .build();
    }

    private RosterCategory category(String raceName, short value, boolean isMinus) {
        return RosterCategory.builder()
                .raceName(raceName)
                .categoryValue(value)
                .isMinus(isMinus)
                .build();
    }
}
