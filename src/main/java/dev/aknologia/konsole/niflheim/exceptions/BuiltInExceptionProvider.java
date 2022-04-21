package dev.aknologia.konsole.niflheim.exceptions;

public interface BuiltInExceptionProvider {
    Dynamic2CommandExceptionType doubleTooLow();

    Dynamic2CommandExceptionType doubleTooHigh();

    Dynamic2CommandExceptionType floatTooLow();

    Dynamic2CommandExceptionType floatTooHigh();

    Dynamic2CommandExceptionType integerTooLow();

    Dynamic2CommandExceptionType integerTooHigh();

    Dynamic2CommandExceptionType longTooLow();

    Dynamic2CommandExceptionType longTooHigh();

    DynamicCommandExceptionType literalIncorrect();

    SimpleCommandExceptionType readerExpectedStartOfQuote();

    SimpleCommandExceptionType readerExpectedEndOfQuote();

    DynamicCommandExceptionType readerInvalidEscape();

    DynamicCommandExceptionType readerInvalidBool();

    SimpleCommandExceptionType readerExpectedBool();

    DynamicCommandExceptionType readerInvalidInt();

    SimpleCommandExceptionType readerExpectedInt();

    DynamicCommandExceptionType readerInvalidLong();

    SimpleCommandExceptionType readerExpectedLong();

    DynamicCommandExceptionType readerInvalidDouble();

    SimpleCommandExceptionType readerExpectedDouble();

    DynamicCommandExceptionType readerInvalidFloat();

    SimpleCommandExceptionType readerExpectedFloat();

    DynamicCommandExceptionType readerInvalidKey();

    SimpleCommandExceptionType readerExpectedKey();

    DynamicCommandExceptionType readerExpectedSymbol();

    SimpleCommandExceptionType dispatcherUnknownCommand();

    SimpleCommandExceptionType dispatcherUnknownArgument();

    Dynamic2CommandExceptionType dispatcherMissingArgument();

    Dynamic3CommandExceptionType dispatcherInvalidArgument();

    SimpleCommandExceptionType dispatcherExpectedArgumentSeparator();

    DynamicCommandExceptionType dispatcherParseException();

    SimpleCommandExceptionType unknownException();
}
