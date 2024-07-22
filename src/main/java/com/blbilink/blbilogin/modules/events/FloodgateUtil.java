package com.blbilink.blbilogin.modules.events;

import com.blbilink.blbilogin.modules.Sqlite;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import static com.blbilink.blbilogin.BlbiLogin.plugin;

public class FloodgateUtil {

	private static LoginAction login = LoginAction.INSTANCE;


	public static boolean isFloodgate(Player player) {
		try {
			FloodgateApi api = FloodgateApi.getInstance();
			return api.isFloodgatePlayer(player.getUniqueId());
		} catch (Exception e) {
			return false;
		}
	}

	public static void openForum(Player player) {
		if (!FloodgateUtil.isFloodgate(player)) {
			player.sendMessage("Error: Unable to process Bedrock player. Please contact an administrator.");
			return;
		}
		boolean isRegistered = Sqlite.getSqlite().playerExists(player.getUniqueId().toString());

		String title = isRegistered ? "Login" : "Register";
		String description = isRegistered
				? "Please, insert your password to login."
				: "Please, insert your password to register a new player.";
		String field_title = "Password";
		String field_example = "Insert password.";
		String field_title_2 = "Confirm password";
		String field_example_2 = "Insert password again.";



		FloodgatePlayer eF = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
		if (eF == null) {
			player.sendMessage("Error: Unable to process Bedrock player. Please contact an administrator.");
			return;
		}

		CustomForm.Builder builder = CustomForm.builder()
				.title(title)
				.label(description)
				.input(field_title, field_example);

		if (!isRegistered) {
			builder.input(field_title_2, field_example_2);
		}

		builder.validResultHandler(response -> {
			try {
				String pass = response.next();
				if (pass == null || pass.isEmpty()) {
					player.sendMessage("Password cannot be empty. Please try again.");
					openForum(player);
					return;
				}

				if (isRegistered) {
					// Login process
					verify(player, pass);
				} else {
					// Registration process
					String confirmPass = response.next();
					if (confirmPass == null || !pass.equals(confirmPass)) {
						player.sendMessage("Passwords do not match. Please try again.");
						openForum(player);
					} else {
						// Here you should hash the password before storing it
						Sqlite.getSqlite().registerPlayer(player.getUniqueId().toString(), player.getName(), pass);
						player.sendMessage("Registration successful. Please log in.");
						openForum(player);
					}
				}
			} catch (Exception e) {
				player.sendMessage("An error occurred. Please try again.");
				e.printStackTrace();
				plugin.getLogger().warning("Error in form processing for player " + player.getName() + ": " + e.getMessage());
				openForum(player);
			}
		});

		builder.closedOrInvalidResultHandler(() -> {
			player.sendMessage("You must complete the form. Please try again.");
			openForum(player);
		});

		eF.sendForm(builder.build());
	}

	private static void verify(Player e, String password) {
		String uuid = e.getUniqueId().toString();
		if (!Sqlite.getSqlite().playerExists(uuid)) {
			Sqlite.getSqlite().registerPlayer(uuid, e.getName(), password);
			openForum(e);
		} else {
			if (login.isCorrect(uuid, password)) {
				login.loginSuccess(e);
			} else {
				openForum(e);
			}
		}
	}
}
