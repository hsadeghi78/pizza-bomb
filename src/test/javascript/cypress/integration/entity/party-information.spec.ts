import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('PartyInformation e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/party-informations*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('party-information');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load PartyInformations', () => {
    cy.intercept('GET', '/api/party-informations*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('party-information');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('PartyInformation').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details PartyInformation page', () => {
    cy.intercept('GET', '/api/party-informations*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('party-information');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('partyInformation');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create PartyInformation page', () => {
    cy.intercept('GET', '/api/party-informations*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('party-information');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('PartyInformation');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit PartyInformation page', () => {
    cy.intercept('GET', '/api/party-informations*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('party-information');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('PartyInformation');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  /* this test is commented because it contains required relationships
  it('should create an instance of PartyInformation', () => {
    cy.intercept('GET', '/api/party-informations*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('party-information');
    cy.wait('@entitiesRequest')
      .then(({ request, response }) => startingEntitiesCount = response.body.length);
    cy.get(entityCreateButtonSelector).click({force: true});
    cy.getEntityCreateUpdateHeading('PartyInformation');

    cy.get(`[data-cy="infoType"]`).select('OTHER');


    cy.get(`[data-cy="infoTitle"]`).type('صندلی haptic باربادوس', { force: true }).invoke('val').should('match', new RegExp('صندلی haptic باربادوس'));


    cy.get(`[data-cy="infoDesc"]`).type('برنامه', { force: true }).invoke('val').should('match', new RegExp('برنامه'));

    cy.setFieldSelectToLastOfEntity('party');

    cy.get(entityCreateSaveButtonSelector).click({force: true});
    cy.scrollTo('top', {ensureScrollable: false});
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/party-informations*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('party-information');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });
  */

  /* this test is commented because it contains required relationships
  it('should delete last instance of PartyInformation', () => {
    cy.intercept('GET', '/api/party-informations*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/party-informations/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('party-information');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({force: true});
        cy.getEntityDeleteDialogHeading('partyInformation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({force: true});
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/party-informations*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('party-information');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
  */
});
