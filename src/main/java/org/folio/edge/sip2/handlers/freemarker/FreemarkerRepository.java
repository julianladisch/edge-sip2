package org.folio.edge.sip2.handlers.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.EnumMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.folio.edge.sip2.parser.Command;

public class FreemarkerRepository {

  private static FreemarkerRepository instance;
  private EnumMap<Command, Template> templates;
  private final Logger log;

  /**
   * Static method to get the only running Freemarker Repository instance.
   */
  public static synchronized FreemarkerRepository getInstance() {
    if (instance == null) {
      instance = new FreemarkerRepository();
    }
    return instance;
  }

  /**
   * Get the template by Command.
   *
   * @param command the Command that serves as a Key to the runtime cache
   * @return Compiled freemarker template
   */
  public Template getFreemarkerTemplate(Command command) {
    return templates.get(command);
  }

  private FreemarkerRepository() {
    log = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    initializeTemplates();
  }

  private void initializeTemplates() {
    templates = new EnumMap<>(Command.class);

    Configuration configuration = new Configuration(Configuration.VERSION_2_3_27);
    configuration.setClassForTemplateLoading(FreemarkerRepository.class, "/templates");
    configuration.setDefaultEncoding("UTF-8");
    configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    configuration.setLogTemplateExceptions(false);
    configuration.setWrapUncheckedExceptions(true);

    addTemplate(Command.ACS_STATUS, "acs-status.ftl", configuration);
    addTemplate(Command.LOGIN_RESPONSE, "LoginResponse.ftl", configuration);
    addTemplate(Command.REQUEST_SC_RESEND, "RequestSCResend.ftl", configuration);
  }

  private void addTemplate(Command commmand, String templateName, Configuration configuration) {

    Template template;

    try {
      template = configuration.getTemplate(templateName);
      template.setBooleanFormat("Y,N");
      templates.put(commmand, template);
    } catch (IOException e) {
      log.error("Error loading template: " + templateName);
    }
  }
}