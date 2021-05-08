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

describe('Branch e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/branches*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('branch');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Branches', () => {
    cy.intercept('GET', '/api/branches*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('branch');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Branch').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Branch page', () => {
    cy.intercept('GET', '/api/branches*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('branch');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('branch');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Branch page', () => {
    cy.intercept('GET', '/api/branches*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('branch');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Branch');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Branch page', () => {
    cy.intercept('GET', '/api/branches*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('branch');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Branch');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  /* this test is commented because it contains required relationships
  it('should create an instance of Branch', () => {
    cy.intercept('GET', '/api/branches*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('branch');
    cy.wait('@entitiesRequest')
      .then(({ request, response }) => startingEntitiesCount = response.body.length);
    cy.get(entityCreateButtonSelector).click({force: true});
    cy.getEntityCreateUpdateHeading('Branch');

    cy.get(`[data-cy="title"]`).type('sensor ای', { force: true }).invoke('val').should('match', new RegExp('sensor ای'));


    cy.get(`[data-cy="branchCode"]`).type('virtual سرپرست ابزار', { force: true }).invoke('val').should('match', new RegExp('virtual سرپرست ابزار'));


    cy.get(`[data-cy="tradeTitle"]`).type('مفهوم', { force: true }).invoke('val').should('match', new RegExp('مفهوم'));


    cy.get(`[data-cy="activationDate"]`).type('2021-05-08').should('have.value', '2021-05-08');


    cy.get(`[data-cy="expirationDate"]`).type('2021-05-08').should('have.value', '2021-05-08');


    cy.get(`[data-cy="activationStatus"]`).should('not.be.checked');
    cy.get(`[data-cy="activationStatus"]`).click().should('be.checked');

    cy.get(`[data-cy="lat"]`).type('96626').should('have.value', '96626');


    cy.get(`[data-cy="address"]`).type('وام بازاریاب HTTP', { force: true }).invoke('val').should('match', new RegExp('وام بازاریاب HTTP'));


    cy.get(`[data-cy="postalCode"]`).type('سالاد سیستم', { force: true }).invoke('val').should('match', new RegExp('سالاد سیستم'));


    cy.get(`[data-cy="description"]`).type('زده کارشناس', { force: true }).invoke('val').should('match', new RegExp('زده کارشناس'));

    cy.setFieldSelectToLastOfEntity('party');

    cy.get(entityCreateSaveButtonSelector).click({force: true});
    cy.scrollTo('top', {ensureScrollable: false});
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/branches*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('branch');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });
  */

  /* this test is commented because it contains required relationships
  it('should delete last instance of Branch', () => {
    cy.intercept('GET', '/api/branches*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/branches/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('branch');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({force: true});
        cy.getEntityDeleteDialogHeading('branch').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({force: true});
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/branches*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('branch');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
  */
});
