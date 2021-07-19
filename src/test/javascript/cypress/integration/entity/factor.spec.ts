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

describe('Factor e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/factors*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('factor');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Factors', () => {
    cy.intercept('GET', '/api/factors*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('factor');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Factor').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Factor page', () => {
    cy.intercept('GET', '/api/factors*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('factor');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('factor');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Factor page', () => {
    cy.intercept('GET', '/api/factors*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('factor');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Factor');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Factor page', () => {
    cy.intercept('GET', '/api/factors*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('factor');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Factor');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  /* this test is commented because it contains required relationships
  it('should create an instance of Factor', () => {
    cy.intercept('GET', '/api/factors*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('factor');
    cy.wait('@entitiesRequest')
      .then(({ request, response }) => startingEntitiesCount = response.body.length);
    cy.get(entityCreateButtonSelector).click({force: true});
    cy.getEntityCreateUpdateHeading('Factor');

    cy.get(`[data-cy="title"]`).type('زنجان آذربایجان', { force: true }).invoke('val').should('match', new RegExp('زنجان آذربایجان'));


    cy.get(`[data-cy="factorCode"]`).type('navigating', { force: true }).invoke('val').should('match', new RegExp('navigating'));


    cy.get(`[data-cy="lastStatus"]`).select('DELIVERY_SUCCESS');


    cy.get(`[data-cy="orderWay"]`).select('PHONE_CALL');


    cy.get(`[data-cy="serving"]`).select('OUTSIDE');


    cy.get(`[data-cy="paymentStateClassId"]`).type('49788').should('have.value', '49788');


    cy.get(`[data-cy="categoryClassId"]`).type('90639').should('have.value', '90639');


    cy.get(`[data-cy="totalPrice"]`).type('32694').should('have.value', '32694');


    cy.get(`[data-cy="discount"]`).type('15341').should('have.value', '15341');


    cy.get(`[data-cy="tax"]`).type('27603').should('have.value', '27603');


    cy.get(`[data-cy="netprice"]`).type('43141').should('have.value', '43141');


    cy.get(`[data-cy="description"]`).type('رهنمود تجاری', { force: true }).invoke('val').should('match', new RegExp('رهنمود تجاری'));

    cy.setFieldSelectToLastOfEntity('buyerParty');

    cy.setFieldSelectToLastOfEntity('sellerParty');

    cy.setFieldSelectToLastOfEntity('deliveryAddress');

    cy.get(entityCreateSaveButtonSelector).click({force: true});
    cy.scrollTo('top', {ensureScrollable: false});
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/factors*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('factor');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });
  */

  /* this test is commented because it contains required relationships
  it('should delete last instance of Factor', () => {
    cy.intercept('GET', '/api/factors*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/factors/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('factor');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({force: true});
        cy.getEntityDeleteDialogHeading('factor').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({force: true});
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/factors*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('factor');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
  */
});
