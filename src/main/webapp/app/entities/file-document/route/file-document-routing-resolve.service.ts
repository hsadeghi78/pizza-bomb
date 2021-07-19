import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFileDocument, FileDocument } from '../file-document.model';
import { FileDocumentService } from '../service/file-document.service';

@Injectable({ providedIn: 'root' })
export class FileDocumentRoutingResolveService implements Resolve<IFileDocument> {
  constructor(protected service: FileDocumentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFileDocument> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fileDocument: HttpResponse<FileDocument>) => {
          if (fileDocument.body) {
            return of(fileDocument.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FileDocument());
  }
}
