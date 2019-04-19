package org.folio.edge.sip2.handlers;

import static org.folio.edge.sip2.parser.Command.LOGIN_RESPONSE;

import freemarker.template.Template;

import org.folio.edge.sip2.handlers.freemarker.FreemarkerRepository;
import org.folio.edge.sip2.parser.Command;
import org.folio.edge.sip2.repositories.ConfigurationRepository;
import org.folio.edge.sip2.repositories.DefaultResourceProvider;
import org.folio.edge.sip2.repositories.IRequestData;
import org.folio.edge.sip2.repositories.IResourceProvider;
import org.folio.edge.sip2.repositories.LoginRepository;

/**
 * Factory class that holds instantiation logic for all ISip2RequestHandlers.
 * This serves as a central point to get the handlers.
 */
public class HandlersFactory {

  private HandlersFactory() {}

  /**
   * Factory method that encapsulates the logic of instantiating a SCStatusHandler.
   *
   * @param configRepo Configuration repository to configuration data.
   * @param resProvider DefaultResourceProvider instance that gets the data from a config file
   * @param freeMarkerTemplate Freemarker template instance
   * @return SCStatusHandler
   */
  public static ISip2RequestHandler getScStatusHandlerInstance(
      ConfigurationRepository configRepo,
      IResourceProvider<Object> resProvider,
      Template freeMarkerTemplate) {
    resProvider = getResourceProvider(resProvider);

    if (configRepo == null) {
      configRepo = new ConfigurationRepository(resProvider);
    }

    freeMarkerTemplate = getCommandTemplate(freeMarkerTemplate,
        Command.ACS_STATUS);

    return new SCStatusHandler(configRepo, freeMarkerTemplate);
  }

  /**
   * Returns a Login Response command handler with the specified arguments.
   *
   * @param loginRepository a store of login data
   * @param resourceProvider a service for interacting with resources
   * @param commandTemplate the template for SIP2 command output
   * @return
   */
  public static ISip2RequestHandler getLoginHandlerInstance(
      LoginRepository loginRepository,
      IResourceProvider<IRequestData> resourceProvider,
      Template commandTemplate) {
    return new LoginHandler(
        loginRepository == null ? new LoginRepository(
            getResourceProvider(resourceProvider))
            : loginRepository,
        getCommandTemplate(commandTemplate, LOGIN_RESPONSE));
  }

  public static ISip2RequestHandler getCheckoutHandlerIntance() {
    return new CheckoutHandler();
  }

  public static ISip2RequestHandler getInvalidMessageHandler() {
    return new InvalidMessageHandler();
  }

  @SuppressWarnings("unchecked")
  private static <T> IResourceProvider<T> getResourceProvider(
      IResourceProvider<T> resourceProvider) {
    if (resourceProvider == null) {
      return (IResourceProvider<T>) new DefaultResourceProvider();
    }

    return resourceProvider;
  }

  private static Template getCommandTemplate(
      Template commandTemplate,
      Command command) {
    if (commandTemplate == null) {
      return FreemarkerRepository.getInstance().getFreemarkerTemplate(command);
    }

    return commandTemplate;
  }

  public static ISip2RequestHandler getACSResendHandler() {
    return new ACSResendHandler();
  }
}