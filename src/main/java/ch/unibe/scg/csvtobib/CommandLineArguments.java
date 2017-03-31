package ch.unibe.scg.csvtobib;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple command line argument manager. Probably could have used something like
 * Apache Commons CLI instead, but ehhh...
 */
public class CommandLineArguments {

	private final String executable;
	private final List<Argument> arguments;
	private final Map<String, List<String>> pargs;

	/**
	 * Creates a new command line argument manager.
	 *
	 * @param args the unprocessed arguments.
	 */
	public CommandLineArguments(String[] args) {
		this(null, args);
	}

	/**
	 * Creates a new command line argument manager.
	 *
	 * @param clazz the main class of the program.
	 * @param args the unprocessed arguments.
	 */
	public CommandLineArguments(Class clazz, String[] args) {
		this.arguments = new ArrayList<>();
		this.pargs = parseArgs(args);

		String exe = null;
		try {
			final String path = clazz.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			final File file = new File(path);
			exe = file.getName();
		} catch (Exception ex) {
		}
		this.executable = exe;
	}

	/**
	 * Adds/registers a new command line argument to the manager.
	 *
	 * @param description the description.
	 * @param postfix the command postfix.
	 * @param aliases the command aliases.
	 * @return the argument.
	 */
	public Argument add(String description, String postfix, String... aliases) {
		final Argument arg = new Argument(description, postfix, aliases);
		this.arguments.add(arg);
		return arg;
	}

	/**
	 * Checks whether all given arguments are set and non-empty.
	 *
	 * @param args the arguments.
	 * @return {@code True} if all arguments are set and non-empty,
	 * {@code False} otherwise.
	 */
	public static boolean areAllSet(Argument... args) {
		for (Argument a : args) {
			if (a.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Prints the program usage.
	 */
	public void printUsage() {
		System.out.println("Usage: ");
		if (executable != null) {
			System.out.print("\n");
			System.out.println(String.format(
					"$ java -jar %s <options>",
					executable
			));
		}
		System.out.print("\n");
		for (Argument arg : this.arguments) {
			System.out.println(String.format(
					"%s %s\n    %s\n",
					getCommandAliases(arg),
					arg.postfix,
					arg.description
			));
		}
	}

	private String getCommandAliases(Argument arg) {
		final StringBuilder sb = new StringBuilder();
		final int n = arg.aliases.length;
		for (int i = 0; i < n; i++) {
			final String a = arg.aliases[i];
			final int len = a.length();
			sb.append((len < 2) ? '-' : "--");
			sb.append(a);
			if ((i + 1) < n) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}

	/**
	 * Parse the command-line arguments.
	 *
	 * @param args Command-line arguments.
	 * @return A map of options (as keys) together with a list of values.
	 */
	private static Map<String, List<String>> parseArgs(String[] args) {
		HashMap<String, List<String>> p = new HashMap<>();
		List<String> values = null;

		for (String arg : args) {
			switch (arg.charAt(0)) {
				case '-':
					if (arg.length() > 1) {
						values = new ArrayList<>();
						if (arg.charAt(1) == '-') {
							p.put(arg.substring(2), values);
						} else {
							p.put(arg.substring(1), values);
						}
					}
					break;

				default:
					if (values == null) {
						values = new ArrayList<>();
						p.put("", values);
					}
					values.add(arg);
					break;
			}
		}

		return p;
	}

	private boolean isArgSet(String... aliases) {
		for (String alias : aliases) {
			if (pargs.containsKey(alias)) {
				return true;
			}
		}
		return false;
	}

	private List<String> getArg(String... aliases) {
		for (String alias : aliases) {
			final List<String> v = pargs.get(alias);
			if (v != null) {
				return v;
			}
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * A command line argument.
	 */
	public class Argument {

		private final String description;
		private final String postfix;
		private final String[] aliases;
		private final boolean isSet;
		private final List<String> values;

		/**
		 * Creates a new command line argument.
		 *
		 * @param description the description.
		 * @param postfix the command postfix.
		 * @param aliases the command aliases.
		 */
		public Argument(String description, String postfix, String... aliases) {
			this.description = description;
			this.postfix = postfix;
			this.aliases = aliases;
			this.isSet = isArgSet(aliases);
			this.values = getArg(aliases);
		}

		/**
		 * Checks whether the command line argument has been set. The value
		 * might still be empty/null.
		 *
		 * @return {@code true} if the argument is set, {@code false} otherwise.
		 */
		public boolean isSet() {
			return isSet;
		}

		/**
		 * Checks whether the value of the command line argument is empty/null.
		 *
		 * @return {@code true} if the argument is empty/null, {@code false}
		 * otherwise.
		 */
		public boolean isEmpty() {
			final String value = getValue();
			return (value == null) || value.isEmpty();
		}

		/**
		 * Returns the (first) value of the command line argument.
		 *
		 * @return the (first) value of the command line argument.
		 */
		public String getValue() {
			if (values.isEmpty()) {
				return "";
			}
			return values.get(0);
		}

		/**
		 * Returns the value of the command line argument as {@code int}.
		 *
		 * @return the value of the command line argument as {@code int}, or
		 * {@code -1}.
		 */
		public int getInteger() {
			try {
				return Integer.parseInt(getValue());
			} catch (NumberFormatException ex) {
				return -1;
			}
		}

		/**
		 * Returns the value of the command line argument as {@code double}.
		 *
		 * @return the value of the argument as {@code double}, or
		 * {@code Double.NAN}.
		 */
		public double getDouble() {
			try {
				return Double.parseDouble(getValue());
			} catch (NumberFormatException ex) {
				return Double.NaN;
			}
		}

		/**
		 * Returns the value(s) of the command line argument as {@code String}.
		 *
		 * @return the value(s) of the command line argument as {@code String}.
		 */
		public String getString() {
			if (values.isEmpty()) {
				return "";
			}
			final StringBuilder sb = new StringBuilder();
			for (int i = 0, n = values.size(); i < n; i++) {
				sb.append(values.get(i));
				if ((i + 1) < n) {
					sb.append(' ');
				}
			}
			return sb.toString();
		}

	}

}
