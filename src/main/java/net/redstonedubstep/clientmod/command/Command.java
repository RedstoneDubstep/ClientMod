package net.redstonedubstep.clientmod.command;

import java.util.function.Function;

import net.redstonedubstep.clientmod.command.parameter.AbstractParameter;
import net.redstonedubstep.clientmod.command.parameter.IntParameter;
import net.redstonedubstep.clientmod.command.parameter.StringParameter;

public class Command {
	public final String prefix;
	private final AbstractParameter<?>[] params;
	private final Function<AbstractParameter<?>[], CommandResult> action;

	public Command(String prefix, Function<AbstractParameter<?>[], CommandResult> action, AbstractParameter<?> param) {
		this(prefix, action, new AbstractParameter[]{param});
	}
	public Command(String prefix, Function<AbstractParameter<?>[], CommandResult> action, AbstractParameter<?>... params) {
		this.prefix = prefix;
		this.action = action;
		this.params = params;
	}
	
	public CommandResult execute(String parameter) {
		CommandResult result = formatParameters(parameter);
		if (result != null) //when something went wrong while formatting parameters
			return result;
		else
			return action.apply(params);
	}

	private CommandResult formatParameters(String parameterIn) {
		String[] stringParams = parameterIn.split(" ", params.length);

		for (int i=0;i<params.length;i++) {
			if (stringParams.length < i+1 || stringParams[i] == null || stringParams[i].isEmpty()) {
				if (params[i].isRequired())
					return CommandResult.NO_PARAMETER;
				else {
					params[i].setToDefault();
					continue;
				}
			}
			if (params[i].isRequired() && (stringParams[i] == null || stringParams[i].isEmpty()))
				return CommandResult.NO_PARAMETER;
			else if (params[i] instanceof StringParameter) {
				StringParameter stringParam = (StringParameter)params[i];

				stringParam.setValue(stringParams[i]);
			}
			if (params[i] instanceof IntParameter) {
				IntParameter intParam = (IntParameter)params[i];
				int value;

				try{
					value = Integer.parseInt(stringParams[i]);
				} catch (NumberFormatException e) {
					return CommandResult.INVALID_PARAMETER;
				}

				if (!intParam.isValueAllowed(value))
					return CommandResult.INVALID_PARAMETER;

				intParam.setValue(value);
			}
		}


		return null;
	}

}
