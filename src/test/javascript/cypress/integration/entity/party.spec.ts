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

describe('Party e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/parties*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('party');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Parties', () => {
    cy.intercept('GET', '/api/parties*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('party');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Party').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Party page', () => {
    cy.intercept('GET', '/api/parties*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('party');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('party');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Party page', () => {
    cy.intercept('GET', '/api/parties*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('party');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Party');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Party page', () => {
    cy.intercept('GET', '/api/parties*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('party');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Party');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of Party', () => {
    cy.intercept('GET', '/api/parties*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('party');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Party');

    cy.get(`[data-cy="title"]`)
      .type('عملکرد خدمات آفریقا', { force: true })
      .invoke('val')
      .should('match', new RegExp('عملکرد خدمات آفریقا'));

    cy.get(`[data-cy="partyCode"]`).type('ابزار', { force: true }).invoke('val').should('match', new RegExp('ابزار'));

    cy.get(`[data-cy="tradeTitle"]`).type('کسب', { force: true }).invoke('val').should('match', new RegExp('کسب'));

    cy.get(`[data-cy="activationDate"]`).type('2021-05-07').should('have.value', '2021-05-07');

    cy.get(`[data-cy="expirationDate"]`).type('2021-05-07').should('have.value', '2021-05-07');

    cy.get(`[data-cy="activationStatus"]`).should('not.be.checked');
    cy.get(`[data-cy="activationStatus"]`).click().should('be.checked');

    cy.get(`[data-cy="description"]`)
      .type('کردستان کرون نکردنی', { force: true })
      .invoke('val')
      .should('match', new RegExp('کردستان کرون نکردنی'));

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/parties*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('party');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of Party', () => {
    cy.intercept('GET', '/api/parties*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/parties/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('party');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('party').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/parties*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('party');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
