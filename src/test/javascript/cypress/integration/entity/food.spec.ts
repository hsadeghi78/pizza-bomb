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

describe('Food e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/foods*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('food');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Foods', () => {
    cy.intercept('GET', '/api/foods*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('food');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Food').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Food page', () => {
    cy.intercept('GET', '/api/foods*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('food');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('food');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Food page', () => {
    cy.intercept('GET', '/api/foods*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('food');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Food');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Food page', () => {
    cy.intercept('GET', '/api/foods*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('food');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Food');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  /* this test is commented because it contains required relationships
  it('should create an instance of Food', () => {
    cy.intercept('GET', '/api/foods*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('food');
    cy.wait('@entitiesRequest')
      .then(({ request, response }) => startingEntitiesCount = response.body.length);
    cy.get(entityCreateButtonSelector).click({force: true});
    cy.getEntityCreateUpdateHeading('Food');

    cy.get(`[data-cy="title"]`).type('خراسان copy', { force: true }).invoke('val').should('match', new RegExp('خراسان copy'));


    cy.get(`[data-cy="foodCode"]`).type('مدیرعامل', { force: true }).invoke('val').should('match', new RegExp('مدیرعامل'));


    cy.get(`[data-cy="sizeClassId"]`).type('67613').should('have.value', '67613');


    cy.setFieldImageAsBytesOfEntity('photo', 'integration-test.png', 'image/png');


    cy.get(`[data-cy="categoryClassId"]`).type('7854').should('have.value', '7854');


    cy.get(`[data-cy="lastPrice"]`).type('56126').should('have.value', '56126');


    cy.get(`[data-cy="description"]`).type('پویا پلاستیکی', { force: true }).invoke('val').should('match', new RegExp('پویا پلاستیکی'));

    cy.setFieldSelectToLastOfEntity('producerParty');

    cy.setFieldSelectToLastOfEntity('designerParty');

    cy.setFieldSelectToLastOfEntity('foodType');

    cy.get(entityCreateSaveButtonSelector).click({force: true});
    cy.scrollTo('top', {ensureScrollable: false});
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/foods*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('food');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });
  */

  /* this test is commented because it contains required relationships
  it('should delete last instance of Food', () => {
    cy.intercept('GET', '/api/foods*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/foods/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('food');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({force: true});
        cy.getEntityDeleteDialogHeading('food').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({force: true});
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/foods*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('food');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
  */
});
