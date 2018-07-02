package org.tron.core.services;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tron.common.application.Service;
import org.tron.core.config.args.Args;
import org.tron.core.services.http.GetAccountServlet;
import org.tron.core.services.http.GetAssetIssueListServlet;
import org.tron.core.services.http.GetBlockByNumServlet;
import org.tron.core.services.http.GetNowBlockServlet;
import org.tron.core.services.http.GetPaginatedAssetIssueListServlet;
import org.tron.core.services.http.ListWitnessesServlet;
import org.tron.core.services.http.solidity.GetTransactionByIdServlet;
import org.tron.core.services.http.solidity.GetTransactionInfoByIdServlet;
import org.tron.core.services.http.solidity.GetTransactionsFromThisServlet;
import org.tron.core.services.http.solidity.GetTransactionsToThisServlet;

@Component
//@Slf4j
public class SolidityNodeHttpApiService implements Service {

  private int port = Args.getInstance().getSolidityHttpPort();

  private Server server;

  @Autowired
  private GetAccountServlet accountServlet;


  @Autowired
  private GetTransactionByIdServlet getTransactionByIdServlet;
  @Autowired
  private GetTransactionInfoByIdServlet getTransactionInfoByIdServlet;
  @Autowired
  private GetTransactionsFromThisServlet getTransactionsFromThisServlet;
  @Autowired
  private GetTransactionsToThisServlet getTransactionsToThisServlet;

  @Autowired
  private ListWitnessesServlet listWitnessesServlet;
  @Autowired
  private GetAssetIssueListServlet getAssetIssueListServlet;
  @Autowired
  private GetPaginatedAssetIssueListServlet getPaginatedAssetIssueListServlet;
  @Autowired
  private GetNowBlockServlet getNowBlockServlet;
  @Autowired
  private GetBlockByNumServlet getBlockByNumServlet;


  @Override
  public void init() {

  }

  @Override
  public void init(Args args) {

  }

  @Override
  public void start() {
    Args args = Args.getInstance();
    try {
      server = new Server(port);
      ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
      context.setContextPath("/");
      server.setHandler(context);

      // same as FullNode
      context.addServlet(new ServletHolder(accountServlet), "/getaccount");
      context.addServlet(new ServletHolder(listWitnessesServlet), "/listwitnesses");
      context.addServlet(new ServletHolder(getAssetIssueListServlet), "/getassetissuelist");
      context.addServlet(new ServletHolder(getPaginatedAssetIssueListServlet),
          "/getpaginatedassetissuelist");
      context.addServlet(new ServletHolder(getNowBlockServlet), "/getnowblock");
      context.addServlet(new ServletHolder(getBlockByNumServlet), "/getblockbynum");

      // only for SolidityNode
      context.addServlet(new ServletHolder(getTransactionByIdServlet), "/gettransactionbyid");
      context
          .addServlet(new ServletHolder(getTransactionInfoByIdServlet), "/gettransactioninfobyid");

      // for extension api
      if (args.isWalletExtensionApi()) {
        context.addServlet(new ServletHolder(getTransactionsFromThisServlet),
            "/gettransactionsfromthisservlet");
        context
            .addServlet(new ServletHolder(getTransactionsToThisServlet),
                "/gettransactionstothisservlet");
      }

      server.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void stop() {
    try {
      server.stop();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}